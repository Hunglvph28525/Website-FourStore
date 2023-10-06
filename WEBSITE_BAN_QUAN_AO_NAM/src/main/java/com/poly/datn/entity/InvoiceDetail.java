package com.poly.datn.entity;
import com.poly.datn.entity.composite.InvoiceId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "InvoiceDetails")
public class InvoiceDetail {
    @EmbeddedId
    private InvoiceId invoiceId;

    @Column(name = "quantity")
    @Min(1)
    private Integer quantity;

    @Column(name = "price")
    private Double price;
}
