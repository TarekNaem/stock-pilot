package com.stock.pilot.repository;

import com.stock.pilot.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    boolean existsByCode(String code);

    long countByActiveTrue();

    @Query("select s from Supplier s where (:keyword is null or :keyword = '' or lower(s.code) like lower(concat('%', :keyword, '%')) or lower(s.name) like lower(concat('%', :keyword, '%'))) and (:active is null or s.active = :active)")
    Page<Supplier> search(@Param("keyword") String keyword, @Param("active") Boolean active, Pageable pageable);
}
