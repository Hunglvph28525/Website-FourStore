package com.poly.datn.controller.user;

import com.poly.datn.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ForGotPasswort {


    private User user;

    @GetMapping("/forgot-passwort/user")
    public String getUserName(@AuthenticationPrincipal Principal principal){
        if (principal == null) {
            return "redirect:/forgot-passwort";
        }
        return "User/forgot-password/user";
    }

}
