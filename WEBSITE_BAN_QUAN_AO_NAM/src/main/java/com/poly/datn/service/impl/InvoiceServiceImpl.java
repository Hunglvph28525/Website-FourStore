package com.poly.datn.service.impl;

import com.poly.datn.dto.InvoiceDetailDto;
import com.poly.datn.dto.InvoiceDto;
import com.poly.datn.entity.Invoice;
import com.poly.datn.entity.InvoiceDetail;
import com.poly.datn.entity.User;
import com.poly.datn.repository.InvoiceDetailRepository;
import com.poly.datn.repository.InvoiceRepository;
import com.poly.datn.service.InvoiceService;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository repository;

    @Autowired
    private InvoiceDetailRepository detailRepository;

    @Override
    public List<Invoice> getAll() {
        List<Invoice> list = repository.getAll();
        list.stream().forEach(x -> {
            if (x.getUser() == null) {
                x.setUser(User.builder().name("Khách lẻ").build());
            }
        });
        return list;
    }

    @Override
    public List<InvoiceDto> fillAll(String status) {
        List<InvoiceDto> list = repository.fillAll(status);
        list.stream().forEach(x -> {
            List<InvoiceDetailDto> products = detailRepository.getAllInvoi(x.getCodeBill());
            x.setProduct(products == null ? products = new ArrayList<>() : products);
        });
        return list;
    }

    @Override
    public List<Invoice> getStatus(String status) {
        List<Invoice> list = repository.getStatus(status);
        list.stream().forEach(x -> {
            if (x.getUser() == null) {
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

    @Override
    public MessageUtil create() {
        if (repository.countAllByStatus("0") < 5) {
            Invoice invoice = Invoice.builder()
                    .codeBill("HD" + generateCodeBill(5))
                    .status("0")
                    .type("1")
                    .note("Đơn hàng được tạo bởi "+ UserUltil.getUser().getName())
                    .createDate(LocalDateTime.now())
                    .total(BigDecimal.valueOf(0))
                    .shippingCost(BigDecimal.valueOf(0))
                    .giaGiam(BigDecimal.valueOf(0))
                    .grandTotal(BigDecimal.valueOf(0))
                    .build();
            repository.save(invoice);
            return new MessageUtil(1, "Tạo đơn hàng thành công", "bg-success", invoice);
        }


        return new MessageUtil(0, "Thất bại tạo tối đa 5 đơn hàng", "bg-danger");
    }

    @Override
    public MessageUtil delete(String codeBill) {
        List<InvoiceDetail> list = detailRepository.getByCodeBill(codeBill);
        detailRepository.deleteAll(list);
        repository.deleteById(codeBill);
        return new MessageUtil(1, "Xóa thành công", "bg-success");

    }

    private String generateCodeBill(int x) {
        String characters = "0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < x; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }


}
