package com.poly.datn.controller.admin;

import com.poly.datn.repository.InvoiceRepository;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class ThongKeController {

    @Autowired
    InvoiceRepository invoiceRepository;

    @GetMapping("/thongkehoadon")
    public String thongKeHoaDon(Model model){
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("chothanhtoan", invoiceRepository.getStatusChoThanhToan());
        model.addAttribute("choxacnhan", invoiceRepository.getStatusChoXacNhan());
        model.addAttribute("chogiaohang", invoiceRepository.getStatusChoGiaoHang());
        model.addAttribute("danggiaohang", invoiceRepository.getStatusDangGiao());
        model.addAttribute("hoanthanh", invoiceRepository.getStatusHoanThanh());
        model.addAttribute("dahuy", invoiceRepository.getStatusDaHuy());


        return "admin/thongkehoadon";
    }

    @GetMapping("/thongkedoanhthu")
    public String thongKe(Model model){
        model.addAttribute("user", UserUltil.getUser());
        return "admin/thongkedoanhthu";
    }

    @GetMapping("/thongkesanpham")
    public String thongKeSanPham(Model model){
        model.addAttribute("user", UserUltil.getUser());
        return "admin/thongkesanpham";
    }



//    @GetMapping("/pieChart")
//    public String pieChart(Model model) {
//        model.addAttribute("pass", invoiceRepository.getStatusDangHoatDong());
//        model.addAttribute("fail", invoiceRepository.getStatusKhongHoatDong());
//
//        System.out.println("số luọng"  +invoiceRepository.getStatusKhongHoatDong());
//        return "pieChart";
//
//    }
}
