package com.poly.datn.repository;

import com.poly.datn.dto.ProductDto;
import com.poly.datn.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select new com.poly.datn.dto.ProductDto(c) from Product c where c.id = :id")
    ProductDto getProduct(Long id);

    @Query("select c from Product c where c.promotion is NULL")
    List<Product> getproductNoPromotion();

    @Query("select c from Product c where c.typeProduct.id = :id")
    List<Product> getProductByTypeProduct(Long id);

    @Query("select c from Product c where c.promotion.id = :id")
    List<Product> getProducByPromotion(Long id);

    @Query("select new com.poly.datn.dto.ProductDto(c) from Product c order by c.id DESC ")
    List<ProductDto> getAll();

    @Query("select new com.poly.datn.dto.ProductDto(c) from Product c where c.status = :status or c.typeProduct.category.id = :category or c.typeProduct.id = :type or c.brand.id = :brand order by c.id desc ")
    List<ProductDto> locProduct(String status, Long category,Long type, Long brand);
}
