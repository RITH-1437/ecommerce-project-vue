package com.demo.backend.repository;

import com.demo.backend.model.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for TrackingEvent entity.
 * Provides methods to retrieve tracking events for shipments.
 */
@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent, Long> {

    /**
     * Find all tracking events for a specific shipment, ordered by timestamp
     * descending.
     */
    List<TrackingEvent> findByShipmentIdOrderByEventTimestampDesc(Long shipmentId);

    /**
     * Find all tracking events for a specific shipment, ordered by timestamp
     * ascending.
     */
    List<TrackingEvent> findByShipmentIdOrderByEventTimestampAsc(Long shipmentId);
}
