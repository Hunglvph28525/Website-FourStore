package com.poly.datn.repository;

import com.poly.datn.dto.TransactionDto;
import com.poly.datn.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("select new com.poly.datn.dto.TransactionDto (c)from Transaction c where c.invoice.codeBill = :codeBill order by c.id asc")
    List<TransactionDto> getAll(String codeBill);

}
