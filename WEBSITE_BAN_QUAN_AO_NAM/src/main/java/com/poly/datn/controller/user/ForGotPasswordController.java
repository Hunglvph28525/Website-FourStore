package com.poly.datn.controller.user;

import com.poly.datn.entity.User;
import com.poly.datn.request.forgot_passwort.UserForgotPasswordRequest;
import com.poly.datn.service.UserService;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@Setter
public class ForGotPasswordController {


    @Autowired
    private UserService userService;

    private UserForgotPasswordRequest userForgotPasswordRequest;

    public ForGotPasswordController() {
        this.userForgotPasswordRequest = new UserForgotPasswordRequest();
    }

    @GetMapping("/forgot-password/user")
    public String getUserName(Principal principal , Model model,
                              RedirectAttributes attributes) {
        model.addAttribute("userForgotPasswordRequest", this.userForgotPasswordRequest);
        if (principal == null) {
            return "User/forgot-password/user";
        }
        this.userForgotPasswordRequest.setUserName(principal.getName());
        attributes.addFlashAttribute("userForgotPasswordRequest", userForgotPasswordRequest);
        return "redirect:/forgot-password";
    }

    @PostMapping("/forgot-password/user")
    public String getUserName(@RequestParam String username) {

        System.out.println("\n\n\n\t" + username + "\n\n\n\n");
        Optional<User> userOptional =userService.getByUserName(username);

        if (userOptional.isPresent()) {
            this.userForgotPasswordRequest.setUserName(username);

            return "redirect:/forgot-password";
        }

        return "redirect:/forgot-password/user";
    }

    @GetMapping("/forgot-password")
    public String forGotPassword(Model model) {

        model.addAttribute("userForgotPasswordRequest", this.userForgotPasswordRequest);
        return "User/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(Model model, @ModelAttribute @Valid UserForgotPasswordRequest userForgotPasswordRequest
        , BindingResult bindingResult) {

        Boolean passwordDoesNotMatch = !userForgotPasswordRequest.getPassword()
            .equals(userForgotPasswordRequest.getConfirmPassword());
        Boolean userNotFound = userService.getByUserName(userForgotPasswordRequest.getUserName()).isEmpty();

        if (bindingResult.hasErrors()) {
            return "User/forgot-password";
        }

        if (passwordDoesNotMatch || userNotFound) {
            model.addAttribute("error", "user doesn't exists aor password not match");
        }

        userService.changePassword(userForgotPasswordRequest);

        return "redirect:/login";
    }
}
