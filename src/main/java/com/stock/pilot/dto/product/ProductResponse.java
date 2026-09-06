package com.stock.pilot.dto.product;

import com.stock.pilot.model.Unit;

import java.util.UUID;

public record ProductResponse(

        UUID id,

        String sku,

        String name,

        String description,

        UUID supplierId,

        String supplierName,

        Unit unit,

        int quantityOnHand,

        int reorderLevel,

        boolean active
) {
}
