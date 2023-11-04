package com.poly.datn.controller.admin;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Category;
import com.poly.datn.entity.TypeProduct;
import com.poly.datn.service.CategoryService;
import com.poly.datn.service.ProductService;
import com.poly.datn.service.PromotionService;
import com.poly.datn.service.TypeProductService;
import com.poly.datn.util.UserUltil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class PromotionController {
    @Autowired
    private PromotionService service;

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TypeProductService typeProductService;

    @GetMapping("/promotion")
    public String promition(Model model, @RequestParam(value = "status", defaultValue = "-1") String status) {
        model.addAttribute("promotions", service.getAll(status));
        model.addAttribute("user", UserUltil.getUser());
        return "admin/khuyenmai";
    }

    @GetMapping("promotion/new")
    public String formAdd(Model model) {
        model.addAttribute("promotion", new PromotionDto());
        model.addAttribute("user", UserUltil.getUser());
        return "admin/add-khuyenmai";
    }

    @PostMapping("/promotion/add")
    public String addPromition(@Valid @ModelAttribute("promotion") PromotionDto dto, Model model,
                               BindingResult result,
                               @RequestParam(value = "sps", required = false) List<Long> list) {
        if (result.hasErrors()) {
            model.addAttribute("user", UserUltil.getUser());
            return "admin/promotion/promotion";
        }
        return "redirect:/admin/promotion";
    }

    @GetMapping("/promotion/{id}")
    public String promotionDetail(@PathVariable Long id, Model model) {
        return "admin/update-khuyenmai";
    }

    @PostMapping("/promotion/{id}")
    public String update(@ModelAttribute("promotion") PromotionDto dto) {
        return "redirect:/admin/promotion";
    }

    @GetMapping("/promotion/delete/{id}")
    public String deletePromotion(@PathVariable("id") Long id) {
        return "redirect:/admin/promotion";
    }



}