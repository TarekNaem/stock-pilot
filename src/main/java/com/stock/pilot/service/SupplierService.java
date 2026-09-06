package com.stock.pilot.service;

import com.stock.pilot.dto.supplier.SupplierRequest;
import com.stock.pilot.dto.supplier.SupplierResponse;
import com.stock.pilot.exception.EntityAlreadyExist;
import com.stock.pilot.exception.ResourceNotFoundException;
import com.stock.pilot.mapper.SupplierMapper;
import com.stock.pilot.model.Supplier;
import com.stock.pilot.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class SupplierService {
    private final SupplierRepository repository;
    private final SupplierMapper mapper;
    public SupplierService(SupplierRepository repository, SupplierMapper mapper) { this.repository = repository; this.mapper = mapper; }

    @Transactional
    public SupplierResponse create(SupplierRequest r) {
        String code = r.code().trim().toUpperCase();
        if (repository.existsByCode(code)) throw new EntityAlreadyExist("Supplier code already exists: " + code);
        Supplier s = new Supplier(UUID.randomUUID(), code, r.name().trim(), r.email(), r.phone(), true, OffsetDateTime.now(), OffsetDateTime.now(), 1);
        return mapper.toResponse(repository.save(s));
    }

    @Transactional(readOnly = true)
    public Page<SupplierResponse> search(String keyword, Boolean active, Pageable pageable) {
        String k = keyword == null ? null : keyword.trim();
        return repository.search(k, active, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public SupplierResponse get(UUID id) { return mapper.toResponse(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id))); }

    @Transactional
    public SupplierResponse update(UUID id, SupplierRequest r) {
        Supplier s = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id));
        String code = r.code().trim().toUpperCase();
        if (!s.getCode().equals(code) && repository.existsByCode(code)) throw new EntityAlreadyExist("Supplier code already exists: " + code);
        s.updateDetails(code, r.name().trim(), r.email(), r.phone());
        return mapper.toResponse(repository.save(s));
    }

    @Transactional
    public void deactivate(UUID id) { Supplier s = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id)); s.deactivate(); repository.save(s); }

    @Transactional
    public void activate(UUID id) { Supplier s = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id)); s.activate(); repository.save(s); }
}
