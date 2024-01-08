package com.poly.datn.service;

import com.poly.datn.dto.InvoiceDto;
import com.poly.datn.dto.UpdateQuantityReq;
import com.poly.datn.entity.Invoice;
import com.poly.datn.entity.User;
import com.poly.datn.util.MessageUtil;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface InvoiceService {
    List<InvoiceDto> getAll();

    List<InvoiceDto> fillAll(String status);

    List<Invoice> getStatus(String status);

    InvoiceDto detail(String codeBill);

    MessageUtil create();

    MessageUtil delete(String codeBill);

    MessageUtil addProduct(String codeBill, Long sp);

    MessageUtil deleteSp(String code, Long sp);

    Map<?,?> updateSL(UpdateQuantityReq req);

    MessageUtil addUser(String codeBill,Long User);

    MessageUtil addUPgg(String code, Long pgg);

    MessageUtil deletePgg(String code);

    MessageUtil payment(String codeBill, Integer paymentMethod, Double tienKhachDua);

    MessageUtil resposePayment(String codeBill, String ghiChu, String payDate, String status);

    MessageUtil newUser(User user, String codeBill);

    MessageUtil addShipping(String codeBill, String city, String district, String ward, String diaChi, String sdt, String name);

    MessageUtil huyGh(String codeBill);

    MessageUtil updateStatus(String codeBill, String note);

    MessageUtil huyDh(String codeBill, String note);

    MessageUtil ttDonHang(String codeBill, String note);

    void deleteInvoice();

    Object getInvoceByUser();

    void xuatHd(String codeBill, HttpServletResponse response) throws IOException;
}
