package com.poly.datn.repository;

import com.poly.datn.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    @Query("select c from Invoice c where c.status in ('1','2','3','4','-1') order by c.createDate desc ")
    List<Invoice> getAll();

    @Query("select c from Invoice c where c.status like :status order by c.createDate desc ")
    List<Invoice> getStatus(String status);

    @Query("select count(status) from Invoice where status = '0'")
    Integer getStatusChoThanhToan();

    @Query("select count(status) from Invoice where status = '1'")
    Integer getStatusChoXacNhan();

    @Query("select count(status) from Invoice where status = '2'")
    Integer getStatusChoGiaoHang();

    @Query("select count(status) from Invoice where status = '3'")
    Integer getStatusDangGiao();

    @Query("select count(status) from Invoice where status = '4'")
    Integer getStatusHoanThanh();

    @Query("select count(status) from Invoice where status = '-1'")
    Integer getStatusDaHuy();






}
