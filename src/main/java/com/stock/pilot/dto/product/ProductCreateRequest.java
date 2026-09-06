package com.stock.pilot.dto.product;

import com.stock.pilot.model.Unit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ProductCreateRequest(

        @NotBlank(message = "SKU is required")
        @Size(max = 50, message = "SKU must not exceed 50 characters")
        String sku,

        @NotBlank(message = "Product name is required")
        @Size(max = 160, message = "Product name must not exceed 160 characters")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        UUID supplierId,

        @NotNull(message = "Unit is required")
        Unit unit,

        @Min(value = 0, message = "Reorder level cannot be negative")
        int reorderLevel
) {
}
