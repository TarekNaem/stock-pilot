package com.stock.pilot.controller;
import com.stock.pilot.dto.product.*; import com.stock.pilot.service.ProductService; import com.stock.pilot.service.InventoryService; import jakarta.validation.Valid; import org.springframework.data.domain.*; import org.springframework.data.web.PageableDefault; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.UUID;
@RestController @RequestMapping("/api/v1/products") public class ProductController { private final ProductService service; private final InventoryService inventory; public ProductController(ProductService service,InventoryService inventory){this.service=service;this.inventory=inventory;}
@PostMapping public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(r));}
@GetMapping public Page<ProductResponse> get(@RequestParam(required=false) String keyword,@RequestParam(required=false) Boolean active,@RequestParam(required=false) Boolean lowStock,@PageableDefault(size=20,sort="name",direction=Sort.Direction.ASC) Pageable p){return inventory.searchProducts(keyword,active,lowStock,p);}
@GetMapping("/{id}") public ProductResponse one(@PathVariable UUID id){return service.getProduct(id);}
@PutMapping("/{id}") public ProductResponse update(@PathVariable UUID id,@Valid @RequestBody ProductCreateRequest r){return service.updateProduct(id,r);}
@PatchMapping("/{id}/activate") @ResponseStatus(HttpStatus.NO_CONTENT) public void activate(@PathVariable UUID id){service.activate(id);}
@PatchMapping("/{id}/deactivate") @ResponseStatus(HttpStatus.NO_CONTENT) public void deactivate(@PathVariable UUID id){service.deactivate(id);}
}
