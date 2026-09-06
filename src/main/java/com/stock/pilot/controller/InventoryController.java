package com.stock.pilot.controller;

import com.stock.pilot.dto.inventory.InventoryDashboardResponse;
import com.stock.pilot.dto.product.ProductResponse;
import com.stock.pilot.service.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/dashboard")
    public InventoryDashboardResponse dashboard() {
        return inventoryService.getDashboard();
    }

    @GetMapping("/products")
    public Page<ProductResponse> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Boolean lowStock,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return inventoryService.searchProducts(keyword, active, lowStock, pageable);
    }

    @GetMapping("/low-stock")
    public Page<ProductResponse> lowStockProducts(
            @PageableDefault(size = 20, sort = "quantityOnHand", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return inventoryService.getLowStockProducts(pageable);
    }
}
