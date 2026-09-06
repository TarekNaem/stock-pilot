package com.stock.pilot.dto.supplier;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SupplierResponse(UUID id, String code, String name, String email, String phone, boolean active, OffsetDateTime createdAt, OffsetDateTime updatedAt, int version) {}
