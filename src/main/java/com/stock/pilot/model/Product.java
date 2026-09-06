package com.stock.pilot.model;

import com.stock.pilot.exception.BusinessException;
import com.stock.pilot.exception.InsufficientStockException;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "product",
        indexes = {
                @Index(
                        name = "ix_product_low_stock",
                        columnList = "is_active, quantity_on_hand, reorder_level"
                )
        }
)
public class Product {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 160)
    private String name;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Unit unit = Unit.EACH;

    @Column(
            name = "quantity_on_hand",
            nullable = false
    )
    private int quantityOnHand = 0;

    @Column(
            name = "reorder_level",
            nullable = false
    )
    private int reorderLevel = 0;

    @Column(
            name = "is_active",
            nullable = false
    )
    private boolean active = true;

    @Column(
            name = "created_at",
            nullable = false
    )
    private OffsetDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private OffsetDateTime updatedAt;

    @Version
    @Column(nullable = false)
    private int version;

    protected Product() {
        // Required by JPA
    }

    public Product(
            String sku,
            String name,
            String description,
            Supplier supplier,
            Unit unit,
            int reorderLevel
    ) {
        this.id = UUID.randomUUID();
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.supplier = supplier;
        this.unit = unit;
        this.quantityOnHand = 0;
        this.reorderLevel = reorderLevel;
        this.active = true;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
        this.version = 1;
    }


    public void updateDetails(String sku, String name, String description, Supplier supplier, Unit unit, int reorderLevel) {
        this.sku = sku; this.name = name; this.description = description; this.supplier = supplier; this.unit = unit; this.reorderLevel = reorderLevel; touch();
    }

    public void receive(int quantity) {

        if (quantity < 1) {
            throw new BusinessException(
                    "Receive quantity must be greater than zero."
            );
        }

        quantityOnHand += quantity;
        touch();
    }

    public void issue(int quantity) {

        if (quantity < 1) {
            throw new BusinessException(
                    "Issue quantity must be greater than zero."
            );
        }

        if (quantity > quantityOnHand) {
            throw new InsufficientStockException(
                    "Insufficient stock. Available: " + quantityOnHand
            );
        }

        quantityOnHand -= quantity;
        touch();
    }

    public boolean isLowStock() {
        return active && quantityOnHand <= reorderLevel;
    }

    public void activate() {
        this.active = true;
        touch();
    }

    public void deactivate() {
        this.active = false;
        touch();
    }

    private void touch() {
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Unit getUnit() {
        return unit;
    }

    public int getQuantityOnHand() {
        return quantityOnHand;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public boolean isActive() {
        return active;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public int getVersion() {
        return version;
    }
}
