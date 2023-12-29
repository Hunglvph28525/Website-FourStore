package com.poly.datn.service;

import com.poly.datn.entity.Invoice;
import com.poly.datn.entity.PaymentMethod;
import com.poly.datn.util.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface PaymentService {
    String paymentOnlineOnCounter(String codeBill,Integer paymentMethod , HttpServletRequest req) throws UnsupportedEncodingException;
    List<PaymentMethod> getPaymentMethods();
    String paymentOnlineOnWeb(Invoice invoice , HttpServletRequest req) throws UnsupportedEncodingException;
}
