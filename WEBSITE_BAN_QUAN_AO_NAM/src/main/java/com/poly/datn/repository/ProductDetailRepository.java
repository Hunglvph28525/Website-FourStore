package com.poly.datn.repository;

import com.poly.datn.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {
    @Query("select c from ProductDetail c where c.product.id = :id")
    List<ProductDetail> getDetailProduct(Long id);

    ProductDetail getById(Long id);

    boolean existsById(Long id);

    @Query("select c from ProductDetail c where c.product.id = :productId and c.color.id =:colorId and c.size.id =:sizeId")
    ProductDetail getProduct(Long productId, Long colorId, Long sizeId);
}
