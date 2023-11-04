package com.poly.datn.controller.admin;

import com.poly.datn.util.UserUltil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes("user")
@RequestMapping("/admin")
@Controller
public class DashboardController {

    @ModelAttribute("user")
    public Object initUser() {
        return UserUltil.getUser();
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("user", UserUltil.getUser());
        return "admin/index";
    }
}
