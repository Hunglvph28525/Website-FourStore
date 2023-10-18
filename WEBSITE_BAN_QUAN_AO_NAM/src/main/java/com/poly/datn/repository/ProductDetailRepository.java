package com.poly.datn.repository;

import com.poly.datn.dto.ProductDetailDto;
import com.poly.datn.entity.ProductDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {
    @Query("select new com.poly.datn.dto.ProductDetailDto(c) from ProductDetail c where c.product.id = :id")
    List<ProductDetailDto> getDetailProduct(Long id);
    ProductDetail getById(Long id);
}
