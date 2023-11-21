package com.poly.datn.repository;

import com.poly.datn.dto.InvoiceDetailDto;
import com.poly.datn.entity.InvoiceDetail;
import com.poly.datn.entity.composite.InvoiceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, InvoiceId> {
    @Query("select new com.poly.datn.dto.InvoiceDetailDto(c) from InvoiceDetail c where c.invoiceId.invoice.codeBill =:codeBill")
    List<InvoiceDetailDto> getAllInvoi(String codeBill);

    @Query("select c from InvoiceDetail c where c.invoiceId.invoice.codeBill =:codeBill")
    List<InvoiceDetail> getByCodeBill(String codeBill);

}
