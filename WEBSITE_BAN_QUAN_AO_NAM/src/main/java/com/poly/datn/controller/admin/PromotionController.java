package com.poly.datn.controller.admin;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Promotion;
import com.poly.datn.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PromotionController {
    @Autowired
    private PromotionService service;

    @GetMapping("/promotion")
    public String promition(Model model) {
        model.addAttribute("list", service.getAll());
        model.addAttribute("object", new Promotion());
        return "admin/promotion/promotion";
    }
    @PostMapping("/promotion/add")
    public String addPromition(@ModelAttribute("object")PromotionDto dto) {
        service.add(dto);
        return "redirect:/admin/promotion";
    }
    @GetMapping("/promotion/{id}")
    public String promotionDetail(Model model){

        return "/admin/promotion/promotion-detail";
    }

}
