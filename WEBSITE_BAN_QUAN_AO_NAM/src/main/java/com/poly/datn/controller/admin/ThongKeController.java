package com.poly.datn.controller.admin;

import com.poly.datn.dto.BieuDoCot;
import com.poly.datn.repository.InvoiceRepository;
import com.poly.datn.repository.ProductRepository;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("admin")
public class ThongKeController {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/thongkehoadon")
    public String thongKeHoaDon(Model model){

        LocalDateTime ngay1 = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime ngay2 = LocalDateTime.of(2024, 1, 31, 0, 0);

        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("chothanhtoan", invoiceRepository.getStatusChoThanhToan());
        model.addAttribute("choxacnhan", invoiceRepository.getStatusChoXacNhan());
        model.addAttribute("chogiaohang", invoiceRepository.getStatusChoGiaoHang());
        model.addAttribute("danggiaohang", invoiceRepository.getStatusDangGiao());
        model.addAttribute("hoanthanh", invoiceRepository.getStatusHoanThanh());
        model.addAttribute("dahuy", invoiceRepository.getStatusDaHuy());
        model.addAttribute("tonghoadon",invoiceRepository.getTongHoaDon());
        model.addAttribute("donhanghoanthanh",invoiceRepository.getDonHangHoanThanh());
        model.addAttribute("donhangdanggiao",invoiceRepository.getDonHangDangGiao());
        model.addAttribute("donhangdahuy",invoiceRepository.getDonHangDaHuy());


      System.out.println("doanhthuthang"+ invoiceRepository.getTimKiemBieuDoCot(ngay1,ngay2));
       model.addAttribute("sanphambanchay",invoiceRepository.getSanPhamBanChay());
       model.addAttribute("doanhthuthangnay",invoiceRepository.getDoanhThuThangNay());
       model.addAttribute("doanhthungayhomnay",invoiceRepository.getDoanhThuHomNay());
       model.addAttribute("doanhthunamnay",invoiceRepository.getDoanhThuNamNay());
       model.addAttribute("sosanphamdabanduocthangnay",invoiceRepository.getSoSanPhamDaBanDuocThangNay());

        return "admin/thongkehoadon";
    }



    @GetMapping("/thongkedoanhthu")
    public String thongKetk(Model model, BieuDoCot bieuDoCot, LocalDateTime ngay1, LocalDateTime ngay2){
        model.addAttribute("user", UserUltil.getUser());
        Map<LocalDateTime, Double> data = new LinkedHashMap<LocalDateTime, Double>();
        List<BieuDoCot> dt = invoiceRepository.getTimKiemBieuDoCot(ngay1,ngay2);
//        System.out.println("thongkebieudo"+ dt);
        for(BieuDoCot d : dt){
            data.put(d.getCreateDate(), d.getPrice());
        }
        model.addAttribute("keySet", data.keySet());
        model.addAttribute("values", data.values());

        model.addAttribute("doanhthuthangnay",invoiceRepository.getDoanhThuThangNay());
        model.addAttribute("doanhthungayhomnay",invoiceRepository.getDoanhThuHomNay());
        model.addAttribute("doanhthunamnay",invoiceRepository.getDoanhThuNamNay());
        model.addAttribute("tongdoanhthu",invoiceRepository.getTongDoanhThu());
        return "admin/thongkedoanhthu";
    }




//    @GetMapping("/thongkedoanhthu")
//    public String thongKe(Model model, BieuDoCot bieuDoCot){
//        model.addAttribute("user", UserUltil.getUser());
//
//
//        Map<LocalDateTime, Double> data = new LinkedHashMap<LocalDateTime, Double>();
//
//        List<BieuDoCot> dt = invoiceRepository.getBieuDoCot();
//
//        System.out.println("doanhthu"+ invoiceRepository.getBieuDoCot());
//
//        for(BieuDoCot d : dt){
//            data.put(d.getCreateDate(), d.getPrice());
//
//        }
////        data.put("Ankit", 50);
////        data.put("Gurpreet", 70);
////        data.put("Mohit", 90);
////        data.put("Manish", 25);
//        model.addAttribute("keySet", data.keySet());
//        model.addAttribute("values", data.values());
//
//
//
//
//        return "admin/thongkedoanhthu";
//    }

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


//    @GetMapping("/timkiembieudo")
//    public String thongKetk(Model model, BieuDoCot bieuDoCot, LocalDateTime ngay1, LocalDateTime ngay2){
//        model.addAttribute("user", UserUltil.getUser());
//        Map<LocalDateTime, Double> data = new LinkedHashMap<LocalDateTime, Double>();
//        List<BieuDoCot> dt = invoiceRepository.getTimKiemBieuDoCot(ngay1,ngay2);
////        System.out.println("thongkebieudo"+ dt);
//        for(BieuDoCot d : dt){
//            data.put(d.getCreateDate(), d.getPrice());
//        }
//        model.addAttribute("keySet", data.keySet());
//        model.addAttribute("values", data.values());
//        return "admin/thongketimkiem";
//    }


}
