package com.stock.pilot.mapper;

import com.stock.pilot.dto.supplier.SupplierResponse;
import com.stock.pilot.model.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {
    public SupplierResponse toResponse(Supplier s) {
        return new SupplierResponse(s.getId(), s.getCode(), s.getName(), s.getEmail(), s.getPhone(), s.isActive(), s.getCreatedAt(), s.getUpdatedAt(), s.getVersion());
    }
}
