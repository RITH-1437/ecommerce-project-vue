package com.demo.backend.model.enums;

/**
 * Enum representing different types of tracking events.
 */
public enum TrackingEventType {
    ORDER_PLACED, // Order has been placed
    ORDER_CONFIRMED, // Order confirmed by seller
    PAYMENT_RECEIVED, // Payment confirmed
    PREPARING_SHIPMENT, // Package being prepared
    SHIPMENT_CREATED, // Shipment label created
    PICKED_UP, // Package picked up by carrier
    IN_TRANSIT, // Package in transit
    ARRIVED_AT_HUB, // Arrived at sorting facility
    DEPARTED_FROM_HUB, // Departed from sorting facility
    CUSTOMS_CLEARANCE, // In customs (for international)
    OUT_FOR_DELIVERY, // Out for delivery
    DELIVERY_ATTEMPTED, // Delivery attempted but failed
    DELIVERED, // Successfully delivered
    EXCEPTION, // Exception occurred
    RETURNED_TO_SENDER, // Returned to sender
    CANCELLED // Shipment cancelled
}
