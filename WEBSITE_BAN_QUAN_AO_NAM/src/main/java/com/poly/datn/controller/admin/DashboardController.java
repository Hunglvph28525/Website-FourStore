package com.poly.datn.controller.admin;

import com.poly.datn.util.UserUltil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class DashboardController {


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("user", UserUltil.getUser());
        return "admin/index";
    }
}
