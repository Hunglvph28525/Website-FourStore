package com.poly.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class StatisticalController {

    @GetMapping("/statictical")
    public String hienThi(){
        return "admin/statistical/index";
    }

    @GetMapping("/statictical/day")
    public String theoNgay(){
        return "admin/statistical/thongketheongay";
    }

    @GetMapping("/statictical/product")
    public String theoSP(){
        return "admin/statistical/thongketheosanpham";
    }




}
