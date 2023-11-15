package com.poly.datn.service;

import com.poly.datn.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> getAll(String codeBill);
}
