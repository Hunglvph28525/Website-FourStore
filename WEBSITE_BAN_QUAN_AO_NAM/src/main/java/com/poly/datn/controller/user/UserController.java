package com.poly.datn.controller.user;

import com.poly.datn.request.UserSignUpRequest;
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
        model.addAttribute("userSignUpRequest",new UserSignUpRequest());
        return "User/sign-up";
    }
    @PostMapping("/sign-up")
    public String dang(Model model , @ModelAttribute @Valid UserSignUpRequest userSignUpRequest,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "User/sign-up";
        }

        if (!userSignUpRequest.getPassword().equals(userSignUpRequest.getConfirmPassword())){
            model.addAttribute("error","mat khau khong khop");
            return "User/sign-up";
        }

        userService.add(userService.convert(userSignUpRequest));
        return "redirect:/login";
    }
}
