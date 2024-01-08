package com.poly.datn.controller.web;

import com.poly.datn.request.UserSignUpRequest;
import com.poly.datn.service.*;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserWebController {
    @Autowired
    private CartService cartService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;


    @PostMapping("/addtocart/{sp}")
    public String addtocart(@PathVariable("sp") Long sp,
                            @RequestParam("color") Long color,
                            @RequestParam("size") Long size,
                            @RequestParam("quantity") Integer quantity,
                            RedirectAttributes attributes) {
        MessageUtil messageUtil = cartService.addtocart(sp, size, color, quantity);
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/shop/" + sp;
    }

    @PostMapping("/buynow/{sp}")
    public String buyNow(@PathVariable("sp") Long sp,
                         @RequestParam("color") Long color,
                         @RequestParam("size") Long size,
                         @RequestParam("quantity") Integer quantity) {
        cartService.addtocart(sp, size, color, quantity);
        return "redirect:/cart";
    }

    @ModelAttribute("giohang")
    public Object initGiohang() {
        return cartService.getGioHang();
    }

    @ModelAttribute("user")
    public Object initUser() {
        return UserUltil.getUser();
    }

    @ModelAttribute("message")
    public MessageUtil initMessage() {
        return new MessageUtil();
    }

    @ModelAttribute("pgg")
    public Object initpgg() {
        return promotionService.getAll("1");
    }

    @ModelAttribute("paymeThod")
    public Object iniPaymetod() {
        return paymentService.getPaymentMethods();
    }

    @ModelAttribute("category")
    public Object initType() {
        return categoryService.getAll();
    }
    @ModelAttribute("userSignUpRequest")
    public Object initDangki() {
        return new UserSignUpRequest();
    }
    @ModelAttribute("brands")
    public Object initbrand() {
        return brandService.getAll();
    }
}
