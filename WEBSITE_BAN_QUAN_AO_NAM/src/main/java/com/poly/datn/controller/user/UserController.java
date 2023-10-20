package com.poly.datn.controller.user;

import com.poly.datn.entity.User;
import com.poly.datn.request.RegisterUser;
import com.poly.datn.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.transform.Result;
import java.security.Principal;


@Controller

public class UserController {

    @Autowired
    private UserService userService;
    @RequestMapping("/login")
    public String login() {
        return "User/login";
    }

    @GetMapping("/")
    public String vanne(Principal principal, Model model ) {
        model.addAttribute("username",principal.getName());
        return "User/hi";
    }

    @GetMapping("/sign-up")
    public String dangky(Model model){
        model.addAttribute("registerUser",new RegisterUser());
        return "User/sign-up";
    }
    @PostMapping("/sign-up")
    public String dang(Model model , @ModelAttribute @Valid RegisterUser registerUser,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "User/sign-up";

        }
        if (!registerUser.getPassword().equals(registerUser.getConfirmPassword())){
            model.addAttribute("error","mat khau khong khop");
            return "User/sign-up";
        }
        userService.add(userService.convert(registerUser));
        return "User/login";

    }
}
