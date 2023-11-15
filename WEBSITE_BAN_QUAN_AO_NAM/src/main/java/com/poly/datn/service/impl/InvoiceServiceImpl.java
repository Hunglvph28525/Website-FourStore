package com.poly.datn.service.impl;

import com.poly.datn.dto.InvoiceDto;
import com.poly.datn.entity.Invoice;
import com.poly.datn.entity.InvoiceDetail;
import com.poly.datn.entity.User;
import com.poly.datn.repository.InvoiceDetailRepository;
import com.poly.datn.repository.InvoiceRepository;
import com.poly.datn.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository repository;

    @Autowired
    private InvoiceDetailRepository detailRepository;

    @Override
    public List<Invoice> getAll() {
        List<Invoice> list = repository.getAll();
        list.stream().forEach(x ->{
            if (x.getUser() == null){
                x.setUser(User.builder().name("Khách lẻ").build());
            }
        });
        return list;
    }

    @Override
    public List<Invoice> getStatus(String status) {
        List<Invoice> list =  repository.getStatus(status);
        list.stream().forEach(x ->{
            if (x.getUser() == null){
                x.setUser(User.builder().name("Khách lẻ").build());
            }
        });
        return list;



    }

    @Override
    public InvoiceDto detail(String codeBill) {
        InvoiceDto invoiceDto = new InvoiceDto(repository.getReferenceById(codeBill));
        invoiceDto.setProduct(detailRepository.getAllInvoi(codeBill));
        return invoiceDto;
    }

}
