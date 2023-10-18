package com.poly.datn.repository;

import com.poly.datn.dto.ProductDto;
import com.poly.datn.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select new com.poly.datn.dto.ProductDto(c) from Product c where c.id = :id")
    ProductDto getProduct(Long id);
}
