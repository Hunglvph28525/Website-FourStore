package com.poly.datn.controller.admin;

import com.poly.datn.dto.BieuDoCot;
import com.poly.datn.dto.BieuDoThongKeThang;
import com.poly.datn.repository.InvoiceRepository;
import com.poly.datn.repository.ProductRepository;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
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



       model.addAttribute("sanphambanchay",invoiceRepository.getSanPhamBanChay());
       model.addAttribute("doanhthuthangnay",invoiceRepository.getDoanhThuThangNay());
       model.addAttribute("doanhthungayhomnay",invoiceRepository.getDoanhThuHomNay());
       model.addAttribute("doanhthunamnay",invoiceRepository.getDoanhThuNamNay());
       model.addAttribute("sosanphamdabanduocthangnay",invoiceRepository.getSoSanPhamDaBanDuocThangNay());

        return "admin/thongkehoadon";
    }

    @GetMapping("/thongkesanpham")
    public  String thongKeSanPhamm(Model model){
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("sanphambanchay",invoiceRepository.getSanPhamBanChay());
        return "admin/thongkesanpham";
    }



    @GetMapping("/thongkedoanhthu")
    public String thongKetk(Model model, BieuDoCot bieuDoCot, Date ngay1, Date ngay2){
        model.addAttribute("user", UserUltil.getUser());
        Map<Date, Double> data = new LinkedHashMap<Date, Double>();
        List<BieuDoCot> dt = invoiceRepository.getTimKiemBieuDoCot(ngay1,ngay2);
//        System.out.println("thongkebieudo"+ dt);
        for(BieuDoCot d : dt){
            data.put(d.getCreateDate(), d.getPrice());
        }
        model.addAttribute("keySet", data.keySet());
        model.addAttribute("values", data.values());



       Double doanhthuhomnay = invoiceRepository.getDoanhThuHomNay();
       if(doanhthuhomnay == null){
           doanhthuhomnay = 0.0;
       }
        Double doanhthuthangnay = invoiceRepository.getDoanhThuThangNay();
        if(doanhthuthangnay == null){
            doanhthuthangnay = 0.0;
        }
        Double doanhthunamnay = invoiceRepository.getDoanhThuNamNay();
        if(doanhthunamnay == null){
            doanhthunamnay = 0.0;
        }
        Double tongdoanhthu = invoiceRepository.getTongDoanhThu();
        if(tongdoanhthu == null){
            tongdoanhthu = 0.0;
        }
        DecimalFormat df = new DecimalFormat("#,###");




//        model.addAttribute("doanhthuthangnay",invoiceRepository.getDoanhThuThangNay());
//        model.addAttribute("doanhthungayhomnay",invoiceRepository.getDoanhThuHomNay());
//        model.addAttribute("doanhthunamnay",invoiceRepository.getDoanhThuNamNay());
//        model.addAttribute("tongdoanhthu",invoiceRepository.getTongDoanhThu());

        model.addAttribute("doanhthuthangnay",df.format(doanhthuthangnay));
        model.addAttribute("doanhthungayhomnay",df.format(doanhthuhomnay));
        model.addAttribute("doanhthunamnay",df.format(doanhthunamnay));
        model.addAttribute("tongdoanhthu",df.format(tongdoanhthu));
        return "admin/thongkedoanhthu";
    }

    //doanh thu 7 ngày trước
    @GetMapping("/doanhthu7ngay")
    public  String thongKe7Ngay(Model model, BieuDoCot bieuDoCot, Date ngay1, Date ngay2){
        model.addAttribute("user", UserUltil.getUser());
        Map<Date, Double> data = new LinkedHashMap<Date, Double>();
        List<BieuDoCot> bd = invoiceRepository.getDoanhThu7NgayTruoc();
        for(BieuDoCot d: bd){
            data.put(d.getCreateDate(),d.getPrice());
        }
        model.addAttribute("keySet", data.keySet());
        model.addAttribute("values", data.values());

        model.addAttribute("doanhthuthangnay",invoiceRepository.getDoanhThuThangNay());
        model.addAttribute("doanhthungayhomnay",invoiceRepository.getDoanhThuHomNay());
        model.addAttribute("doanhthunamnay",invoiceRepository.getDoanhThuNamNay());
        model.addAttribute("tongdoanhthu",invoiceRepository.getTongDoanhThu());
        return  "admin/thongkedoanhthu7ngay";
    }


    //tim kiem





    //doanh thu 1 tháng trước
    @GetMapping("/doanhthu1thang")
    public  String thongKe1Thang(Model model, BieuDoCot bieuDoCot){
        model.addAttribute("user", UserUltil.getUser());
        Map<Date, Double> data = new LinkedHashMap<Date, Double>();
        List<BieuDoCot> bd = invoiceRepository.getDoanhThu1ThangTruoc();
        for(BieuDoCot d: bd){
            data.put(d.getCreateDate(),d.getPrice());
        }
        model.addAttribute("keySet", data.keySet());
        model.addAttribute("values", data.values());

        model.addAttribute("doanhthuthangnay",invoiceRepository.getDoanhThuThangNay());
        model.addAttribute("doanhthungayhomnay",invoiceRepository.getDoanhThuHomNay());
        model.addAttribute("doanhthunamnay",invoiceRepository.getDoanhThuNamNay());
        model.addAttribute("tongdoanhthu",invoiceRepository.getTongDoanhThu());
        return  "admin/thongkedoanhthu1thang";
    }

    //doanh thu 6 tháng trước
    @GetMapping("/doanhthu6thang")
    public  String thongKe6Thang(Model model, BieuDoThongKeThang bieuDoCot){
        model.addAttribute("user", UserUltil.getUser());
        Map<Integer, Double> data = new LinkedHashMap<Integer, Double>();
        List<BieuDoThongKeThang> bd = invoiceRepository.getDoanhThu6ThangTruoc();
        for(BieuDoThongKeThang d: bd){
            data.put(d.getCreateDate(),d.getPrice());
        }
        model.addAttribute("keySet", data.keySet());
        model.addAttribute("values", data.values());

        model.addAttribute("doanhthuthangnay",invoiceRepository.getDoanhThuThangNay());
        model.addAttribute("doanhthungayhomnay",invoiceRepository.getDoanhThuHomNay());
        model.addAttribute("doanhthunamnay",invoiceRepository.getDoanhThuNamNay());
        model.addAttribute("tongdoanhthu",invoiceRepository.getTongDoanhThu());
        return  "admin/thongkedoanhthu6thang";
    }

    //sản phẩm bán chạy ngày
    @GetMapping("/banchayngay")
    public  String banChayNgay(Model model){
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("sanphambanchayngay",invoiceRepository.getSPBanChayNgay());
        return "admin/thongkesanphamngay";
    }

    //sản phẩm bán chạy tuần
    @GetMapping("/banchaytuan")
    public  String banChayTuan(Model model){
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("sanphambanchaytuan",invoiceRepository.getSPBanChayTuan());
        return "admin/thongkesanphamtuan";
    }

    //sản phẩm bán chạy tháng
    @GetMapping("/banchaythang")
    public  String banChayThang(Model model){
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("sanphambanchaythang",invoiceRepository.getSPBanChayThang());
        return "admin/thongkesanphamthang";
    }

    //sản phẩm bán chạy năm
    @GetMapping("/banchaynam")
    public  String banChayNam(Model model){
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("sanphambanchaynam",invoiceRepository.getSPBanChayNam());
        return "admin/thongkesanphamnam";
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
