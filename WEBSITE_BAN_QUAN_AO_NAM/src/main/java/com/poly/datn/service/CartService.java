package com.poly.datn.service;

import com.poly.datn.dto.GioHangDto;
import com.poly.datn.dto.OrderDetailDto;
import com.poly.datn.dto.SelectedProduct;
import com.poly.datn.util.MessageUtil;

import java.util.List;

public interface CartService {
    MessageUtil addtocart(Long sp, Long size,Long color, Integer quantity);

    GioHangDto  getGioHang();

    MessageUtil createOrder(List<SelectedProduct> selectedProducts);

    MessageUtil deleteProduct(Long idSp);

    MessageUtil newAddressOrder(OrderDetailDto detailDto, String name, String sdt, String province, String district, String ward, String street);

    MessageUtil editAddressOrder(OrderDetailDto detailDto, Long idAddress);

    MessageUtil addPgg(OrderDetailDto detailDto, Long idPgg);

    MessageUtil deleteCart(List<SelectedProduct> selectedProducts);
}
