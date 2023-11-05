package com.poly.datn.controller.admin;

import com.poly.datn.util.UserUltil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


@SessionAttributes("user")
@Controller
@RequestMapping("/admin")
public class InvoiceController {
    @GetMapping("/invoice")
    public String invoice(Model model) {
        return "admin/hoa-don";
    }
    @ModelAttribute("user")
    public Object initUser(){
        return UserUltil.getUser();
    }
}
