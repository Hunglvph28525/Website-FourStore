package com.poly.datn.dto;

import com.poly.datn.entity.Invoice;
import com.poly.datn.entity.Transaction;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TransactionDto {
    private long id;

    private Invoice invoice;

    private String note;

    private String nameUpdate;

    private String status;

    @DateTimeFormat(pattern = "HH:mm' - 'dd/MM/yyyy")
    private LocalDateTime createDate;

    public TransactionDto(Transaction x) {
        this.id = x.getId();
        this.invoice = x.getInvoice();
        this.note = x.getNote();
        this.nameUpdate = x.getName();
        this.status = x.getStatus();
        this.createDate = x.getCreateDate();
    }
}
