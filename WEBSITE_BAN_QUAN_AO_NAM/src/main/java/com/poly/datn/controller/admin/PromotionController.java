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

@Controller
@RequestMapping("/admin")
public class PromotionController {
    @Autowired
    private PromotionService service;

    @GetMapping("/promotion")
    public String promition(@RequestParam(defaultValue = "0", name = "page") Integer pageNum,Model model) {
        Page<Promotion> page = service.phanTrang(pageNum, 10);
        model.addAttribute("list", page);
//        model.addAttribute("list", service.getAll());
        model.addAttribute("object", new Promotion());
        return "admin/promotion/promotion";
    }
    @PostMapping("/promotion/add")
    public String addPromition(@ModelAttribute("object")PromotionDto dto) {
        service.add(dto);
        return "redirect:/admin/promotion";
    }
    @GetMapping("/promotion/{id}")
    public String promotionDetail(@PathVariable Long id, Model model){
        Promotion promotion = service.detail(id);
        model.addAttribute("list",promotion);
        return "/admin/promotion/promotion-update";
    }

//    @GetMapping("view-update/{id}")
//    public String viewUpdate(@PathVariable Long id, Model model) {
//        Promotion promotion = promotionService.detail(id);
//        model.addAttribute("att", promotion);
//        return "promotion/update";
//    }
//
//    @PostMapping("update")
//    public String update(@Valid @ModelAttribute("list") Promotion promotion, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            return "promotion/update";
//        }
//
//        promotion.setStatus(promotion.getStatus());
//        promotion.setDiscountName(promotion.getDiscountName());
//        promotion.setStartDate(promotion.getStartDate());
//        promotion.setEndDate(promotion.getEndDate());
//        promotion.setDiscountValue(promotion.getDiscountValue());
//        promotion.setDiscountType(promotion.getDiscountType());
//
//
//        model.addAttribute("list", promotion);
//        service.save(promotion);
//        return "redirect:/admin/promotion";
//    }

    @PostMapping("/promotion/update/{id}")
    public String updateProduct(@ModelAttribute("list") PromotionDto dto, @PathVariable("id") Long id){
        service.updateP(dto,id);
        return "redirect:/admin/promotion";
    }




}
