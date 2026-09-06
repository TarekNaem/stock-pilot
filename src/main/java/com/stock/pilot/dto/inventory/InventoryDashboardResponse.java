package com.stock.pilot.dto.inventory;

public record InventoryDashboardResponse(
        long totalActiveProducts,
        long totalStockUnits,
        long lowStockProducts,
        long outOfStockProducts,
        long totalActiveSuppliers
) {
}
