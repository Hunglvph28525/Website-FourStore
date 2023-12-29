package com.poly.datn.controller.user;

import com.poly.datn.dto.GioHangDto;
import com.poly.datn.request.UserSignUpRequest;
import com.poly.datn.service.UserService;
import com.poly.datn.util.MessageUtil;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;


@Controller
@Setter
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login")
    public String login() {
        return "web/login";
    }

    @GetMapping("/sign-up")
    public String dangky(Model model) {
        model.addAttribute("userSignUpRequest", new UserSignUpRequest());
        return "User/sign-up";
    }

    @PostMapping("/sign-up")
    public String dang(Model model, @ModelAttribute @Valid UserSignUpRequest userSignUpRequest,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "User/sign-up";
        }
        if (userService.isEmailExists(userSignUpRequest.getEmail())) {
            model.addAttribute("emailExists", "email da ton tai");
            userSignUpRequest.setPassword("");
            userSignUpRequest.setConfirmPassword("");
            System.out.println("\n\n\n email da ton tai \n\n\n");
            return "User/sign-up";
        }
        if (!userSignUpRequest.getPassword().equals(userSignUpRequest.getConfirmPassword())) {
            model.addAttribute("error", "mat khau khong khop");
            return "User/sign-up";
        }

        userService.add(userService.convert(userSignUpRequest));
        return "redirect:/login";
    }

    @ModelAttribute("giohang")
    public Object initGiohang() {
//        if (UserUltil.getUser() == null)
        return GioHangDto.builder().tongSp(0L).total(Double.valueOf(0)).cartDetails(new ArrayList<>()).build();
//        return cartService.getGioHang();
    }

    @ModelAttribute("message")
    public MessageUtil initMessage() {
        return new MessageUtil();
    }
}