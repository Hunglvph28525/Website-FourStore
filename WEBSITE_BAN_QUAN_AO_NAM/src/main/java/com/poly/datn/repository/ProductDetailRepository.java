package com.poly.datn.repository;

import com.poly.datn.entity.ProductDetail;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {
    ProductDetail getById(Long id);
}
