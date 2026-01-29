package com.demo.backend.controller;

import com.demo.backend.model.Shipment;
import com.demo.backend.model.TrackingEvent;
import com.demo.backend.model.enums.ShipmentStatus;
import com.demo.backend.model.enums.TrackingEventType;
import com.demo.backend.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing shipments and tracking information.
 */
@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    /**
     * Create a new shipment for an order.
     * POST /api/shipments
     */
    @PostMapping
    public ResponseEntity<Shipment> createShipment(@RequestBody Map<String, Object> payload) {
        Long orderId = Long.valueOf(payload.get("orderId").toString());
        String carrier = (String) payload.get("carrier");
        String trackingNumber = (String) payload.get("trackingNumber");
        String trackingUrl = (String) payload.get("trackingUrl");

        LocalDateTime estimatedDelivery = null;
        if (payload.get("estimatedDeliveryDate") != null) {
            estimatedDelivery = LocalDateTime.parse(payload.get("estimatedDeliveryDate").toString());
        }

        return ResponseEntity.ok(shipmentService.createShipment(
                orderId, carrier, trackingNumber, trackingUrl, estimatedDelivery));
    }

    /**
     * Get shipment by order ID.
     * GET /api/shipments/order/{orderId}
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Shipment> getShipmentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(shipmentService.getShipmentByOrderId(orderId));
    }

    /**
     * Get shipment by tracking number.
     * GET /api/shipments/tracking/{trackingNumber}
     */
    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<Shipment> getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(shipmentService.getShipmentByTrackingNumber(trackingNumber));
    }

    /**
     * Update shipment status.
     * PUT /api/shipments/{shipmentId}/status
     */
    @PutMapping("/{shipmentId}/status")
    public ResponseEntity<Shipment> updateShipmentStatus(
            @PathVariable Long shipmentId,
            @RequestBody Map<String, Object> payload) {

        ShipmentStatus status = ShipmentStatus.valueOf((String) payload.get("status"));
        String location = (String) payload.get("location");
        String notes = (String) payload.get("notes");

        return ResponseEntity.ok(shipmentService.updateShipmentStatus(
                shipmentId, status, location, notes));
    }

    /**
     * Get tracking events for a shipment.
     * GET /api/shipments/{shipmentId}/tracking-events
     */
    @GetMapping("/{shipmentId}/tracking-events")
    public ResponseEntity<List<TrackingEvent>> getTrackingEvents(@PathVariable Long shipmentId) {
        return ResponseEntity.ok(shipmentService.getTrackingEvents(shipmentId));
    }

    /**
     * Add a tracking event to a shipment.
     * POST /api/shipments/{shipmentId}/tracking-events
     */
    @PostMapping("/{shipmentId}/tracking-events")
    public ResponseEntity<TrackingEvent> addTrackingEvent(
            @PathVariable Long shipmentId,
            @RequestBody Map<String, Object> payload) {

        TrackingEventType eventType = TrackingEventType.valueOf((String) payload.get("eventType"));
        String description = (String) payload.get("description");
        String location = (String) payload.get("location");

        LocalDateTime eventTimestamp = payload.get("eventTimestamp") != null
                ? LocalDateTime.parse(payload.get("eventTimestamp").toString())
                : LocalDateTime.now();

        return ResponseEntity.ok(shipmentService.addTrackingEvent(
                shipmentId, eventType, description, location, eventTimestamp));
    }

    /**
     * Mark shipment as delivered.
     * PUT /api/shipments/{shipmentId}/deliver
     */
    @PutMapping("/{shipmentId}/deliver")
    public ResponseEntity<Shipment> markAsDelivered(
            @PathVariable Long shipmentId,
            @RequestBody Map<String, String> payload) {

        String deliveryNotes = payload.get("deliveryNotes");
        String recipientName = payload.get("recipientName");

        return ResponseEntity.ok(shipmentService.markAsDelivered(
                shipmentId, deliveryNotes, recipientName));
    }

    /**
     * Get shipments by status.
     * GET /api/shipments/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Shipment>> getShipmentsByStatus(@PathVariable ShipmentStatus status) {
        return ResponseEntity.ok(shipmentService.getShipmentsByStatus(status));
    }
}
