package com.stock.pilot.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(
            nullable = false,
            unique = true,
            length = 254
    )
    private String email;

    @Column(
            name = "display_name",
            nullable = false,
            length = 120
    )
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private UserRole role;

    @Column(
            name = "created_at",
            nullable = false
    )
    private OffsetDateTime createdAt;

    protected AppUser() {
        // Required by JPA
    }

    public AppUser(
            UUID id,
            String email,
            String displayName,
            UserRole role,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserRole getRole() {
        return role;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isManager() {
        return role == UserRole.MANAGER;
    }

    public boolean isClerk() {
        return role == UserRole.CLERK;
    }
}
