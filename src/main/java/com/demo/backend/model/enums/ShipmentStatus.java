package com.demo.backend.model.enums;

/**
 * Enum representing the different states of a shipment.
 */
public enum ShipmentStatus {
    PENDING, // Shipment created but not yet picked up
    PICKED_UP, // Package picked up by carrier
    IN_TRANSIT, // Package in transit to destination
    OUT_FOR_DELIVERY, // Package is out for delivery
    DELIVERED, // Package successfully delivered
    DELIVERY_FAILED, // Delivery attempt failed
    RETURNED, // Package returned to sender
    CANCELLED // Shipment cancelled
}
