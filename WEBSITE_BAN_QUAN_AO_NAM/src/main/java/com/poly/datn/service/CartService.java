package com.poly.datn.service;

import com.poly.datn.dto.GioHangDto;
import com.poly.datn.dto.SelectedProduct;
import com.poly.datn.util.MessageUtil;

import java.util.List;

public interface CartService {
    MessageUtil addtocart(Long sp, Long size,Long color, Integer quantity);

    GioHangDto  getGioHang();

    MessageUtil createOrder(List<SelectedProduct> selectedProducts);
}
