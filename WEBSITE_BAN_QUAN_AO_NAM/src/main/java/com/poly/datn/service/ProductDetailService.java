package com.poly.datn.service;

import com.poly.datn.entity.ProductDetail;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDetailService {

    List<ProductDetail> getAll();

    Page<ProductDetail> phanTrang(Integer pageNum, Integer pageNo);

    void add(ProductDetail productDetail);

    ProductDetail detail(Long id);

    List<ProductDetail> getDetail(Long id);

    Long delete(Long id);

    void update(List<Long> ids, List<Integer> quantitys, List<BigDecimal> prices, List<String> status);

}
