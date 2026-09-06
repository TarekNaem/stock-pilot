package com.stock.pilot.model;

import com.stock.pilot.exception.InsufficientStockException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void lowStockShouldBeTrueWhenQuantityReachesReorderLevel() {
        Product product = new Product("SKU-1", "Keyboard", null, null, Unit.EACH, 5);

        product.receive(5);

        assertTrue(product.isLowStock());
    }

    @Test
    void lowStockShouldBeFalseWhenQuantityIsAboveReorderLevel() {
        Product product = new Product("SKU-2", "Mouse", null, null, Unit.EACH, 5);

        product.receive(6);

        assertFalse(product.isLowStock());
    }

    @Test
    void issueShouldRejectQuantityGreaterThanAvailableStock() {
        Product product = new Product("SKU-3", "Monitor", null, null, Unit.EACH, 2);
        product.receive(3);

        assertThrows(InsufficientStockException.class, () -> product.issue(4));
        assertEquals(3, product.getQuantityOnHand());
    }
}
