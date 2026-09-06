package com.stock.pilot.service;

import com.stock.pilot.dto.inventory.InventoryDashboardResponse;
import com.stock.pilot.dto.product.ProductResponse;
import com.stock.pilot.mapper.ProductMapper;
import com.stock.pilot.model.Product;
import com.stock.pilot.repository.ProductRepository;
import com.stock.pilot.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock ProductRepository productRepository;
    @Mock SupplierRepository supplierRepository;
    @Mock ProductMapper productMapper;

    @InjectMocks InventoryService inventoryService;

    @Test
    void dashboardShouldAggregateInventoryMetrics() {
        when(productRepository.countByActiveTrue()).thenReturn(10L);
        when(productRepository.sumActiveQuantityOnHand()).thenReturn(125L);
        when(productRepository.countActiveLowStock()).thenReturn(3L);
        when(productRepository.countByActiveTrueAndQuantityOnHand(0)).thenReturn(2L);
        when(supplierRepository.countByActiveTrue()).thenReturn(4L);

        InventoryDashboardResponse response = inventoryService.getDashboard();

        assertEquals(10L, response.totalActiveProducts());
        assertEquals(125L, response.totalStockUnits());
        assertEquals(3L, response.lowStockProducts());
        assertEquals(2L, response.outOfStockProducts());
        assertEquals(4L, response.totalActiveSuppliers());
    }

    @Test
    void searchShouldDelegateWithPaginationAndFilters() {
        Pageable pageable = PageRequest.of(1, 5);
        Product product = mock(Product.class);
        ProductResponse mapped = mock(ProductResponse.class);

        when(productRepository.search("phone", true, true, pageable))
                .thenReturn(new PageImpl<>(List.of(product), pageable, 1));
        when(productMapper.toResponse(product)).thenReturn(mapped);

        var result = inventoryService.searchProducts(" phone ", true, true, pageable);

        assertEquals(1, result.getTotalElements());
        assertSame(mapped, result.getContent().get(0));
        verify(productRepository).search("phone", true, true, pageable);
    }
}
