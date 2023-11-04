package com.poly.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_invoice", referencedColumnName = "code_bill")
    private Invoice invoice;

    @Column(name = "note",columnDefinition = ("nvarchar(max)"))
    private String note;

    @Column(name = "edit_date")
    private Date editDate;

    @Column(name = "status",columnDefinition = ("nvarchar(255)"))
    private String status;

    @Column(name = "create_date")
    private Date createDate;
}
