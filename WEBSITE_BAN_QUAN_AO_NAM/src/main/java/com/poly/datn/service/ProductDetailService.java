package com.poly.datn.service;

import com.poly.datn.dto.ProductDetailDto;
import com.poly.datn.entity.ProductDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductDetailService {

    List<ProductDetail> getAll();

    Page<ProductDetail> phanTrang(Integer pageNum, Integer pageNo);

    void add(ProductDetail productDetail);

    ProductDetail detail(Long id);

    List<ProductDetailDto> getDetail(Long id);
//    void delete(Integer id);
    void update(Long id,Integer quantity);
}
