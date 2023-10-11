package com.poly.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class DashboardController {
    @GetMapping("/dashboard")
    public String dashboard(){
        return "admin/index";
    }
}
