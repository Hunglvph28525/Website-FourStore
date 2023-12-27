package com.poly.datn.controller.web;

import com.poly.datn.dto.GioHangDto;
import com.poly.datn.service.CartService;
import com.poly.datn.service.PromotionService;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping("/user")
public class UserWebController {
    @Autowired
    private CartService cartService;
    @Autowired
    private PromotionService promotionService;

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
                         @RequestParam("quantity") Integer quantity,
                         RedirectAttributes attributes) {
//        MessageUtil messageUtil = cartService.addtocart(sp,size,color,quantity);
//        attributes.addFlashAttribute("message", messageUtil);
        System.out.println(cartService.getGioHang().getTotal());
        return "redirect:/home";
    }

    @ModelAttribute("giohang")
    public Object initGiohang(){
        return cartService.getGioHang();
    }
    @ModelAttribute("message")
    public MessageUtil initMessage() {
        return new MessageUtil();
    }
    @ModelAttribute("pgg")
    public Object initpgg() {
        return promotionService.getAll("1");
    }

}
