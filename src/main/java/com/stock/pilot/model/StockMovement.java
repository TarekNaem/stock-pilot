package com.stock.pilot.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "stock_movement",
        indexes = {
                @Index(
                        name = "ix_movement_product_occurred",
                        columnList = "product_id, occurred_at"
                ),
                @Index(
                        name = "ix_movement_occurred",
                        columnList = "occurred_at"
                )
        }
)
public class StockMovement {

    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "movement_type",
            nullable = false,
            length = 20
    )
    private MovementType movementType;

    @Column(nullable = false)
    private int quantity;

    @Column(length = 80)
    private String reference;

    @Column(length = 250)
    private String note;

    @Column(
            name = "occurred_at",
            nullable = false
    )
    private OffsetDateTime occurredAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "recorded_by",
            nullable = false
    )
    private AppUser recordedBy;

    @Column(
            name = "created_at",
            nullable = false
    )
    private OffsetDateTime createdAt;

    protected StockMovement() {
        // Required by JPA
    }

    private StockMovement(
            UUID id,
            Product product,
            MovementType movementType,
            int quantity,
            String reference,
            String note,
            OffsetDateTime occurredAt,
            AppUser recordedBy,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.product = product;
        this.movementType = movementType;
        this.quantity = quantity;
        this.reference = reference;
        this.note = note;
        this.occurredAt = occurredAt;
        this.recordedBy = recordedBy;
        this.createdAt = createdAt;
    }

    public static StockMovement create(
            Product product,
            MovementType movementType,
            int quantity,
            String reference,
            String note,
            OffsetDateTime occurredAt,
            AppUser recordedBy
    ) {

        if (quantity < 1) {
            throw new IllegalArgumentException(
                    "Movement quantity must be greater than zero."
            );
        }

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product is required."
            );
        }

        if (movementType == null) {
            throw new IllegalArgumentException(
                    "Movement type is required."
            );
        }

        if (recordedBy == null) {
            throw new IllegalArgumentException(
                    "Recorded by user is required."
            );
        }

        if (occurredAt == null) {
            throw new IllegalArgumentException(
                    "Occurred at is required."
            );
        }

        return new StockMovement(
                UUID.randomUUID(),
                product,
                movementType,
                quantity,
                reference,
                note,
                occurredAt,
                recordedBy,
                OffsetDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReference() {
        return reference;
    }

    public String getNote() {
        return note;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    public AppUser getRecordedBy() {
        return recordedBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
