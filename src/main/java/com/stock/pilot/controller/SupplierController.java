package com.stock.pilot.controller;

import com.stock.pilot.dto.supplier.SupplierRequest;
import com.stock.pilot.dto.supplier.SupplierResponse;
import com.stock.pilot.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/suppliers")
@PreAuthorize("hasRole('MANAGER')")
public class SupplierController {
    private final SupplierService service;
    public SupplierController(SupplierService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public SupplierResponse create(@Valid @RequestBody SupplierRequest r) { return service.create(r); }
    @GetMapping public Page<SupplierResponse> search(@RequestParam(required=false) String keyword, @RequestParam(required=false) Boolean active, @PageableDefault(size=20, sort="name", direction=Sort.Direction.ASC) Pageable p) { return service.search(keyword, active, p); }
    @GetMapping("/{id}") public SupplierResponse get(@PathVariable UUID id) { return service.get(id); }
    @PutMapping("/{id}") public SupplierResponse update(@PathVariable UUID id, @Valid @RequestBody SupplierRequest r) { return service.update(id, r); }
    @PatchMapping("/{id}/activate") @ResponseStatus(HttpStatus.NO_CONTENT) public void activate(@PathVariable UUID id) { service.activate(id); }
    @PatchMapping("/{id}/deactivate") @ResponseStatus(HttpStatus.NO_CONTENT) public void deactivate(@PathVariable UUID id) { service.deactivate(id); }
}
