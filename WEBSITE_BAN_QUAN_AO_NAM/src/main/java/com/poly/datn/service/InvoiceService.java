package com.poly.datn.service;

import com.poly.datn.dto.InvoiceDto;
import com.poly.datn.entity.Invoice;
import com.poly.datn.util.MessageUtil;

import java.util.List;

public interface InvoiceService {
    List<Invoice> getAll();
    List<InvoiceDto> fillAll(String status);
    List<Invoice> getStatus(String status);
    InvoiceDto detail(String codeBill);
    MessageUtil create();

    MessageUtil delete(String codeBill);
}
