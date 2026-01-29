package com.demo.backend.repository;

import com.demo.backend.model.Shipment;
import com.demo.backend.model.enums.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Shipment entity.
 * Provides methods to retrieve shipment information.
 */
@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    /**
     * Find shipment by order ID.
     */
    Optional<Shipment> findByOrderId(Long orderId);

    /**
     * Find shipment by tracking number.
     */
    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    /**
     * Find all shipments by status.
     */
    List<Shipment> findByStatus(ShipmentStatus status);

    /**
     * Find all shipments by carrier.
     */
    List<Shipment> findByCarrier(String carrier);
}
