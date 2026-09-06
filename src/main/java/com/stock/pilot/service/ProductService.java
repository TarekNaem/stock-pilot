package com.stock.pilot.service;

import com.stock.pilot.dto.product.ProductCreateRequest;
import com.stock.pilot.dto.product.ProductResponse;
import com.stock.pilot.exception.DuplicateSkuException;
import com.stock.pilot.exception.ResourceNotFoundException;
import com.stock.pilot.mapper.ProductMapper;
import com.stock.pilot.model.Product;
import com.stock.pilot.model.Supplier;
import com.stock.pilot.repository.ProductRepository;
import com.stock.pilot.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

    public ProductService(
            ProductRepository productRepository,
            SupplierRepository supplierRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponse createProduct(
            ProductCreateRequest request
    ) {

        if (productRepository.existsBySku(request.sku())) {
            throw new DuplicateSkuException(
                    "Product with SKU '" + request.sku() + "' already exists."
            );
        }

        Supplier supplier = null;

        if (request.supplierId() != null) {
            supplier = supplierRepository
                    .findById(request.supplierId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Supplier not found: "
                                            + request.supplierId()
                            )
                    );
        }

        Product product = new Product(
                request.sku(),
                request.name(),
                request.description(),
                supplier,
                request.unit(),
                request.reorderLevel()
        );

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }
    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID id) { return productMapper.toResponse(productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id))); }

    @Transactional(readOnly = true)
    public Page<ProductResponse> listProducts(Pageable pageable) { return productRepository.findAll(pageable).map(productMapper::toResponse); }

    @Transactional
    public ProductResponse updateProduct(UUID id, ProductCreateRequest request) {
        Product p = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        if (productRepository.existsBySkuAndIdNot(request.sku(), id)) throw new DuplicateSkuException("Product with SKU '" + request.sku() + "' already exists.");
        Supplier supplier = request.supplierId() == null ? null : supplierRepository.findById(request.supplierId()).orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + request.supplierId()));
        p.updateDetails(request.sku().trim(), request.name().trim(), request.description(), supplier, request.unit(), request.reorderLevel());
        return productMapper.toResponse(productRepository.save(p));
    }

    @Transactional
    public void deactivate(UUID id) { Product p=productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id)); p.deactivate(); productRepository.save(p); }

    @Transactional
    public void activate(UUID id) { Product p=productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id)); p.activate(); productRepository.save(p); }

}
