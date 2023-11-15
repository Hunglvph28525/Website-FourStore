package com.poly.datn.service.impl;

import com.poly.datn.dto.TransactionDto;
import com.poly.datn.repository.TransactionRepository;
import com.poly.datn.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository repository;

    @Override
    public List<TransactionDto> getAll(String codeBill) {
        return repository.getAll(codeBill);
    }
}
