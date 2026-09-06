package com.stock.pilot.service;

import com.stock.pilot.dto.inventory.InventoryDashboardResponse;
import com.stock.pilot.dto.product.ProductResponse;
import com.stock.pilot.mapper.ProductMapper;
import com.stock.pilot.repository.ProductRepository;
import com.stock.pilot.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InventoryService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

    public InventoryService(
            ProductRepository productRepository,
            SupplierRepository supplierRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.productMapper = productMapper;
    }

    public InventoryDashboardResponse getDashboard() {
        long totalActiveProducts = productRepository.countByActiveTrue();
        long totalStockUnits = productRepository.sumActiveQuantityOnHand();
        long lowStockProducts = productRepository.countActiveLowStock();
        long outOfStockProducts = productRepository.countByActiveTrueAndQuantityOnHand(0);
        long totalActiveSuppliers = supplierRepository.countByActiveTrue();

        return new InventoryDashboardResponse(
                totalActiveProducts,
                totalStockUnits,
                lowStockProducts,
                outOfStockProducts,
                totalActiveSuppliers
        );
    }

    public Page<ProductResponse> searchProducts(
            String keyword,
            Boolean active,
            Boolean lowStock,
            Pageable pageable
    ) {
        String normalizedKeyword = keyword == null ? null : keyword.trim();

        return productRepository
                .search(normalizedKeyword, active, lowStock, pageable)
                .map(productMapper::toResponse);
    }

    public Page<ProductResponse> getLowStockProducts(Pageable pageable) {
        return productRepository
                .findLowStock(pageable)
                .map(productMapper::toResponse);
    }
}
