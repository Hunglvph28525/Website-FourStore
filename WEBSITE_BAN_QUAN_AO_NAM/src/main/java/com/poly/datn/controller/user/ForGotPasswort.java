package com.poly.datn.controller.user;

import com.poly.datn.entity.User;
import com.poly.datn.request.forgot_passwort.UserForgotPasswordRequest;
import com.poly.datn.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class ForGotPasswort {


    private User user;
    private UserForgotPasswordRequest userForgotPasswordRequest;

    @Autowired
    private UserService userService;
    @GetMapping("/forgot-password/user")
    public String getUserName(@AuthenticationPrincipal Principal principal ,Model model){
        model.addAttribute("userForgotPasswordRequest",new UserForgotPasswordRequest());
        if (principal == null) {
            return "User/forgot-password/user";
        }
        return "User/forgot-password/user";
    }
    @PostMapping("/forgot-password/user")
    public String getUserName(@ModelAttribute @Valid UserForgotPasswordRequest userForgotPasswordRequest,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "User/forgot-password/user";
        }
        Optional<User> userOptional =userService.getByUserName(userForgotPasswordRequest.getUserName());
        if (userOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("user",userOptional.get());
            return "redirect:/forgot-password";
        }
        return "redirect:/forgot-password/user";
    }

    @GetMapping("/forgot-password")
    public String forGotPassword(){
        return "User/forgot-password";
    }


}
