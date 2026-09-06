package com.stock.pilot.repository;

import com.stock.pilot.model.MovementType;
import com.stock.pilot.model.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {
    @Query("select m from StockMovement m where (:productId is null or m.product.id = :productId) and (:type is null or m.movementType = :type) and (:from is null or m.occurredAt >= :from) and (:to is null or m.occurredAt < :to) order by m.occurredAt desc")
    Page<StockMovement> search(@Param("productId") UUID productId, @Param("type") MovementType type, @Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to, Pageable pageable);

    @Query("select count(m) from StockMovement m where (:from is null or m.occurredAt >= :from) and (:to is null or m.occurredAt < :to)")
    long countInRange(@Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to);

    @Query("select coalesce(sum(case when m.movementType in (com.stock.pilot.model.MovementType.RECEIPT, com.stock.pilot.model.MovementType.ADJUSTMENT_IN) then m.quantity else 0 end),0) from StockMovement m where (:from is null or m.occurredAt >= :from) and (:to is null or m.occurredAt < :to)")
    long receivedInRange(@Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to);

    @Query("select coalesce(sum(case when m.movementType in (com.stock.pilot.model.MovementType.ISSUE, com.stock.pilot.model.MovementType.ADJUSTMENT_OUT) then m.quantity else 0 end),0) from StockMovement m where (:from is null or m.occurredAt >= :from) and (:to is null or m.occurredAt < :to)")
    long issuedInRange(@Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to);
}
