package com.poly.datn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trang-chu")
public class LayoutController {

    @GetMapping("/hien-thi")
    public  String hienThi(){
        return "layout/navbar";
    }
}
