package com.poly.datn.service.impl;

import com.poly.datn.entity.ProductDetail;
import com.poly.datn.repository.ProductDetailRepository;
import com.poly.datn.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Override
    public List<ProductDetail> getAll() {
        return null;
    }

    @Override
    public Page<ProductDetail> phanTrang(Integer pageNum, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNum, pageNo);
        return productDetailRepository.findAll(pageable);

    }

    @Override
    public void add(ProductDetail product) {
        productDetailRepository.save(product);

    }

    @Override
    public ProductDetail detail(Long id) {
        return null;
    }

    @Override
    public List<ProductDetail> getDetail(Long id) {
        return productDetailRepository.getDetailProduct(id);
    }

    @Override
    public Long delete(Long id) {
        ProductDetail productDetail = productDetailRepository.getReferenceById(id);
        Long kq = productDetail.getProduct().getId();
        productDetailRepository.delete(productDetail);
        return kq;
    }

    @Override
    public void update(List<Long> ids, List<Integer> quantitys, List<BigDecimal> prices, List<String> status) {
        ProductDetail detail = new ProductDetail();
        for (int i = 0; i < ids.size(); i++) {
            detail = productDetailRepository.getReferenceById(ids.get(i));
            detail.setQuantity(quantitys.get(i));
            detail.setPrice(prices.get(i));
            detail.setStatus(status.get(i));
            productDetailRepository.save(detail);
        }
    }


}
