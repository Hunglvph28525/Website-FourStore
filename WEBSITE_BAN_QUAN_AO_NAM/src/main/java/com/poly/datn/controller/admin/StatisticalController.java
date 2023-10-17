package com.poly.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/thong-ke")
public class StatisticalController {

    @GetMapping("/hien-thi")
    public String hienThi(){
        return "statistical/index";
    }

    @GetMapping("/thong-ke-theo-ngay")
    public String theoNgay(){
        return "statistical/thongketheongay";
    }

    @GetMapping("/thong-ke-theo-san-pham")
    public String theoSP(){
        return "statistical/thongketheosanpham";
    }




}
