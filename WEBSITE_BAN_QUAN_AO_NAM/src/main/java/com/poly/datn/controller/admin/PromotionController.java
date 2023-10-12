package com.poly.datn.controller.admin;

import com.poly.datn.entity.Category;
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
@RequestMapping("/khuyen-mai")
public class PromotionController {

    @Autowired
    PromotionService promotionService;

    @GetMapping("/hien-thi")
    public String hienThi(@RequestParam(defaultValue = "0", name = "page") Integer pageNum, Model model) {
        Page<Promotion> page = promotionService.phanTrang(pageNum, 1);
        model.addAttribute("list", page);
        model.addAttribute("att", new Promotion());
        return "promotion/index";
    }

    @GetMapping("/view-add")
    public  String viewAdd(Model model){
        model.addAttribute("att", new Promotion());
        return "promotion/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("att") Promotion promotion, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "promotion/index";
        }

        promotion.setStatus("1");
        promotion.setDiscountName(promotion.getDiscountName());
        promotion.setStartDate(promotion.getStartDate());
        promotion.setEndDate(promotion.getEndDate());
        promotion.setDiscountValue(promotion.getDiscountValue());
        promotion.setDiscountType(promotion.getDiscountType());
        promotion.setCteateDate(promotion.getCteateDate());
        promotion.setEditDate(promotion.getEditDate());

        model.addAttribute("att", promotion);
        promotionService.add(promotion);
        return "redirect:/khuyen-mai/hien-thi";
    }


    @GetMapping("view-update/{id}")
    public String viewUpdate(@PathVariable Long id, Model model) {
        Promotion promotion = promotionService.detail(id);
        model.addAttribute("att", promotion);
        return "promotion/update";
    }

    @PostMapping("update")
    public String update(@Valid @ModelAttribute("att") Promotion promotion, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "promotion/update";
        }

        promotion.setStatus(promotion.getStatus());
        promotion.setDiscountName(promotion.getDiscountName());
        promotion.setStartDate(promotion.getStartDate());
        promotion.setEndDate(promotion.getEndDate());
        promotion.setDiscountValue(promotion.getDiscountValue());
        promotion.setDiscountType(promotion.getDiscountType());
        promotion.setCteateDate(promotion.getCteateDate());
        promotion.setEditDate(promotion.getEditDate());

        model.addAttribute("att", promotion);
        promotionService.add(promotion);
        return "redirect:/khuyen-mai/hien-thi";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        promotionService.delete(id);
        return "redirect:/khuyen-mai/hien-thi";
    }



}
