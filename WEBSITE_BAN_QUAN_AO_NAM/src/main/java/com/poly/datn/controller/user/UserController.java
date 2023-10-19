package com.poly.datn.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller

public class UserController {

    @RequestMapping("/login")
    public String login() {
        return "User/login";
    }

    @GetMapping("/")
    public String vanne(Principal principal, Model model ) {
        model.addAttribute("username",principal.getName());
        return "User/hi";
    }
}
