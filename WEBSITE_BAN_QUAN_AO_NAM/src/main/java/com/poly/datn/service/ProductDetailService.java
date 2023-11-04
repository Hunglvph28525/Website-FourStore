package com.poly.datn.service;

import com.poly.datn.entity.ProductDetail;
import com.poly.datn.util.MessageUtil;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDetailService {

    void add(ProductDetail productDetail);

    ProductDetail detail(Long id);

    List<ProductDetail> getDetail(Long id);

    MessageUtil delete(Long id);

    MessageUtil update(List<Long> ids, List<Integer> quantitys, List<BigDecimal> prices);

}
