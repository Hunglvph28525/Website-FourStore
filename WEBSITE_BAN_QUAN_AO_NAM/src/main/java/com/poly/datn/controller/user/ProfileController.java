package com.poly.datn.controller.user;

import com.poly.datn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.print.Printable;
import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getProfile(Principal principal, Model model){
        model.addAttribute("user",userService.getByUserName(principal.getName()));
        return "/User/profile";

    }
}
