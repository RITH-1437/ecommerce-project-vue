package com.demo.backend.service.impl;

import com.demo.backend.model.Order;
import com.demo.backend.model.Shipment;
import com.demo.backend.model.TrackingEvent;
import com.demo.backend.model.enums.ShipmentStatus;
import com.demo.backend.model.enums.TrackingEventType;
import com.demo.backend.repository.OrderRepository;
import com.demo.backend.repository.ShipmentRepository;
import com.demo.backend.repository.TrackingEventRepository;
import com.demo.backend.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for managing shipments and tracking.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final TrackingEventRepository trackingEventRepository;

    @Override
    public Shipment createShipment(Long orderId, String carrier, String trackingNumber,
            String trackingUrl, LocalDateTime estimatedDeliveryDate) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        Shipment shipment = Shipment.builder()
                .order(order)
                .carrier(carrier)
                .trackingNumber(trackingNumber)
                .trackingUrl(trackingUrl)
                .estimatedDeliveryDate(estimatedDeliveryDate)
                .status(ShipmentStatus.PENDING)
                .build();

        Shipment savedShipment = shipmentRepository.save(shipment);

        // Create initial tracking event
        addTrackingEvent(savedShipment.getId(), TrackingEventType.SHIPMENT_CREATED,
                "Shipment label created", null, LocalDateTime.now());

        return savedShipment;
    }

    @Override
    public Shipment updateShipmentStatus(Long shipmentId, ShipmentStatus status,
            String currentLocation, String notes) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + shipmentId));

        shipment.setStatus(status);
        if (currentLocation != null) {
            shipment.setCurrentLocation(currentLocation);
        }

        // Update timestamps based on status
        if (status == ShipmentStatus.PICKED_UP && shipment.getShippedAt() == null) {
            shipment.setShippedAt(LocalDateTime.now());
        } else if (status == ShipmentStatus.DELIVERED && shipment.getDeliveredAt() == null) {
            shipment.setDeliveredAt(LocalDateTime.now());
        }

        Shipment updatedShipment = shipmentRepository.save(shipment);

        // Create tracking event for status change
        TrackingEventType eventType = mapStatusToEventType(status);
        addTrackingEvent(shipmentId, eventType,
                "Shipment status updated to " + status.name(),
                currentLocation, LocalDateTime.now());

        return updatedShipment;
    }

    @Override
    @Transactional(readOnly = true)
    public Shipment getShipmentByOrderId(Long orderId) {
        return shipmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Shipment not found for order id: " + orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public Shipment getShipmentByTrackingNumber(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Shipment not found with tracking number: " + trackingNumber));
    }

    @Override
    public TrackingEvent addTrackingEvent(Long shipmentId, TrackingEventType eventType,
            String description, String location,
            LocalDateTime eventTimestamp) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + shipmentId));

        TrackingEvent event = TrackingEvent.builder()
                .shipment(shipment)
                .eventType(eventType)
                .description(description)
                .location(location)
                .eventTimestamp(eventTimestamp)
                .build();

        return trackingEventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackingEvent> getTrackingEvents(Long shipmentId) {
        return trackingEventRepository.findByShipmentIdOrderByEventTimestampAsc(shipmentId);
    }

    @Override
    public Shipment markAsDelivered(Long shipmentId, String deliveryNotes, String recipientName) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + shipmentId));

        shipment.setStatus(ShipmentStatus.DELIVERED);
        shipment.setDeliveredAt(LocalDateTime.now());
        shipment.setDeliveryNotes(deliveryNotes);
        shipment.setRecipientName(recipientName);

        Shipment updatedShipment = shipmentRepository.save(shipment);

        // Create delivery tracking event
        addTrackingEvent(shipmentId, TrackingEventType.DELIVERED,
                "Package delivered successfully to " + recipientName,
                null, LocalDateTime.now());

        return updatedShipment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shipment> getShipmentsByStatus(ShipmentStatus status) {
        return shipmentRepository.findByStatus(status);
    }

    /**
     * Helper method to map ShipmentStatus to TrackingEventType
     */
    private TrackingEventType mapStatusToEventType(ShipmentStatus status) {
        return switch (status) {
            case PENDING -> TrackingEventType.SHIPMENT_CREATED;
            case PICKED_UP -> TrackingEventType.PICKED_UP;
            case IN_TRANSIT -> TrackingEventType.IN_TRANSIT;
            case OUT_FOR_DELIVERY -> TrackingEventType.OUT_FOR_DELIVERY;
            case DELIVERED -> TrackingEventType.DELIVERED;
            case DELIVERY_FAILED -> TrackingEventType.DELIVERY_ATTEMPTED;
            case RETURNED -> TrackingEventType.RETURNED_TO_SENDER;
            case CANCELLED -> TrackingEventType.CANCELLED;
        };
    }
}
