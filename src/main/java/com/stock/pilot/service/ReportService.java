package com.stock.pilot.service;

import com.stock.pilot.dto.report.InventoryReportResponse;
import com.stock.pilot.repository.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class ReportService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final StockMovementRepository stockMovementRepository;

    public ReportService(
            ProductRepository productRepository,
            SupplierRepository supplierRepository,
            StockMovementRepository stockMovementRepository) {

        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public InventoryReportResponse inventory(
            OffsetDateTime from,
            OffsetDateTime to) {

        long received =
                stockMovementRepository.receivedInRange(from, to);

        long issued =
                stockMovementRepository.issuedInRange(from, to);

        return new InventoryReportResponse(
                productRepository.countByActiveTrue(),
                productRepository.sumActiveQuantityOnHand(),
                productRepository.countActiveLowStock(),
                productRepository.countByActiveTrueAndQuantityOnHand(0),
                supplierRepository.countByActiveTrue(),
                stockMovementRepository.countInRange(from, to),
                received,
                issued,
                received - issued,
                from,
                to
        );
    }
}
