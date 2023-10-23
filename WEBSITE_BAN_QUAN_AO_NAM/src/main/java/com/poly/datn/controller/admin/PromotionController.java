package com.poly.datn.controller.admin;

import com.poly.datn.dto.ProductDto;
import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Promotion;
import com.poly.datn.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
public class PromotionController {
    @Autowired
    private PromotionService service;

    @GetMapping("/promotion")
    public String promition(@RequestParam(defaultValue = "0", name = "page") Integer pageNum,Model model) {
        Page<Promotion> page = service.phanTrang(pageNum, 5);
        model.addAttribute("list", page);
//        model.addAttribute("list", service.getAll());
        model.addAttribute("object", new Promotion());

        return "admin/promotion/promotion";
    }
    @PostMapping("/promotion/add")
    public String addPromition(@Valid @ModelAttribute("object")PromotionDto dto, Model model, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/promotion/promotion";
        }
        service.add(dto);
        return "redirect:/admin/promotion";
    }
    @GetMapping("/promotion/{id}")
    public String promotionDetail(@PathVariable Long id, Model model){
        Promotion promotion = service.detail(id);
        model.addAttribute("list",promotion);
        return "/admin/promotion/promotion-update";
    }


    @PostMapping("/promotion/update/{id}")
    public String updateProduct(@ModelAttribute("list") PromotionDto dto, @PathVariable("id") Long id){
        service.updateP(dto,id);
        return "redirect:/admin/promotion";
    }




}
