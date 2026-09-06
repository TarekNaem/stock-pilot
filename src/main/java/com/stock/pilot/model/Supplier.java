package com.stock.pilot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "supplier")
public class Supplier {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(
            nullable = false,
            unique = true,
            length = 20
    )
    private String code;

    @Column(
            nullable = false,
            length = 160
    )
    private String name;

    @Column(length = 254)
    private String email;

    @Column(length = 40)
    private String phone;

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

    protected Supplier() {
        // Required by JPA
    }

    public Supplier(
            UUID id,
            String code,
            String name,
            String email,
            String phone,
            boolean active,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt,
            int version
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public void updateDetails(String code, String name, String email, String phone) {
        this.code = code;
        this.name = name;
        this.email = email;
        this.phone = phone;
        touch();
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

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
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
