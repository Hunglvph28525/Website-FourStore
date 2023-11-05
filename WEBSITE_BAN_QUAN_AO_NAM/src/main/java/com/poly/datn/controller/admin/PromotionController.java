package com.poly.datn.controller.admin;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.service.PromotionService;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@SessionAttributes("user")
@Controller
@RequestMapping("/admin")
public class PromotionController {
    @Autowired
    private PromotionService service;

    @GetMapping("/promotion")
    public String promition(Model model, @RequestParam(value = "status", defaultValue = "-1") String status) {
        model.addAttribute("promotions", service.getAll(status));
        return "admin/khuyenmai";
    }

    @GetMapping("promotion/new")
    public String formAdd() {
        return "admin/add-khuyenmai";
    }

    @PostMapping("/promotion/add")
    public String addPromition(@ModelAttribute("promotion") PromotionDto dto, RedirectAttributes attributes) {
        MessageUtil messageUtil = service.add(dto);
        attributes.addFlashAttribute("message", messageUtil);
        if (messageUtil.getStatus() == 0) {
            System.out.println("đã vào đây");
            attributes.addFlashAttribute("promotion", messageUtil.getObject());
            return "redirect:/admin/promotion/new";
        } else {
            return "redirect:/admin/promotion";
        }
    }

    @GetMapping("/promotion/{id}")
    public String promotionDetail(@PathVariable Long id, Model model) {
        model.addAttribute("promotion",service.detail(id));
        return "admin/update-khuyenmai";
    }

    @PostMapping("/promotion/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("promotion") PromotionDto dto,RedirectAttributes attributes) {
        MessageUtil messageUtil = service.update(dto, id);
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/admin/promotion";
    }


    @ModelAttribute("user")
    public Object initUser() {
        return UserUltil.getUser();
    }

    @ModelAttribute("promotion")
    public Object initPromotion() {
        return new PromotionDto();
    }
    @ModelAttribute("message")
    public Object initmesage() {
        return new MessageUtil();
    }
}