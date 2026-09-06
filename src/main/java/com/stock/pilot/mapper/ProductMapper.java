package com.stock.pilot.mapper;

import com.stock.pilot.dto.product.ProductResponse;
import com.stock.pilot.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getSupplier() != null
                        ? product.getSupplier().getId()
                        : null,
                product.getSupplier() != null
                        ? product.getSupplier().getName()
                        : null,
                product.getUnit(),
                product.getQuantityOnHand(),
                product.getReorderLevel(),
                product.isActive()
        );
    }
}
