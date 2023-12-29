package com.poly.datn.service;

import com.poly.datn.dto.OrderDetailDto;
import com.poly.datn.util.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface OderService {
    String paymentOrder(String note, Integer payId, OrderDetailDto detailDto, HttpServletRequest request) throws UnsupportedEncodingException;

    MessageUtil resposePayment(String codeBill, String ghiChu, String payDate, String status);
}
