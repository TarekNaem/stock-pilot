package com.stock.pilot.repository;

import com.stock.pilot.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, UUID id);

    @Query("""
            select p from Product p
            where (:keyword is null or :keyword = ''
                   or lower(p.sku) like lower(concat('%', :keyword, '%'))
                   or lower(p.name) like lower(concat('%', :keyword, '%')))
              and (:active is null or p.active = :active)
              and (:lowStock is null
                   or (:lowStock = true and p.active = true and p.quantityOnHand <= p.reorderLevel)
                   or (:lowStock = false and (p.active = false or p.quantityOnHand > p.reorderLevel)))
            """)
    Page<Product> search(
            @Param("keyword") String keyword,
            @Param("active") Boolean active,
            @Param("lowStock") Boolean lowStock,
            Pageable pageable
    );

    @Query("select p from Product p where p.active = true and p.quantityOnHand <= p.reorderLevel")
    Page<Product> findLowStock(Pageable pageable);

    long countByActiveTrue();

    long countByActiveTrueAndQuantityOnHand(int quantityOnHand);

    @Query("select count(p) from Product p where p.active = true and p.quantityOnHand <= p.reorderLevel")
    long countActiveLowStock();

    @Query("select coalesce(sum(p.quantityOnHand), 0) from Product p where p.active = true")
    long sumActiveQuantityOnHand();
}
