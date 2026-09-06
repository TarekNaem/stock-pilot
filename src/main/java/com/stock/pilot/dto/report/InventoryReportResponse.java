package com.stock.pilot.dto.report;

import java.time.OffsetDateTime;

public record InventoryReportResponse(long activeProducts, long stockUnits, long lowStockProducts,
                                      long outOfStockProducts, long activeSuppliers, long totalMovements,
                                      long receivedUnits, long issuedUnits, long netMovement, OffsetDateTime from,
                                      OffsetDateTime to) {
}
