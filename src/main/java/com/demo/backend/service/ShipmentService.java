package com.demo.backend.service;

import com.demo.backend.model.Shipment;
import com.demo.backend.model.TrackingEvent;
import com.demo.backend.model.enums.ShipmentStatus;
import com.demo.backend.model.enums.TrackingEventType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for managing shipments.
 */
public interface ShipmentService {

    /**
     * Create a new shipment for an order.
     */
    Shipment createShipment(Long orderId, String carrier, String trackingNumber,
            String trackingUrl, LocalDateTime estimatedDeliveryDate);

    /**
     * Update shipment status.
     */
    Shipment updateShipmentStatus(Long shipmentId, ShipmentStatus status,
            String currentLocation, String notes);

    /**
     * Get shipment by order ID.
     */
    Shipment getShipmentByOrderId(Long orderId);

    /**
     * Get shipment by tracking number.
     */
    Shipment getShipmentByTrackingNumber(String trackingNumber);

    /**
     * Add tracking event to shipment.
     */
    TrackingEvent addTrackingEvent(Long shipmentId, TrackingEventType eventType,
            String description, String location,
            LocalDateTime eventTimestamp);

    /**
     * Get all tracking events for a shipment.
     */
    List<TrackingEvent> getTrackingEvents(Long shipmentId);

    /**
     * Mark shipment as delivered.
     */
    Shipment markAsDelivered(Long shipmentId, String deliveryNotes, String recipientName);

    /**
     * Get all shipments by status.
     */
    List<Shipment> getShipmentsByStatus(ShipmentStatus status);
}
