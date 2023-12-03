package com.poly.datn.service;

import com.poly.datn.util.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface PaymentService {
    String paymentOnlineOnCounter(String codeBill,Integer paymentMethod , HttpServletRequest req) throws UnsupportedEncodingException;
}
