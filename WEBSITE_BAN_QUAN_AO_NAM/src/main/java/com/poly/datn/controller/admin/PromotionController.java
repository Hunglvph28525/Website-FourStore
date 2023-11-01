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
    public String promition(Model model, @RequestParam(value = "status", defaultValue = "-1") String status,
                            @RequestParam(value = "start",required = false)LocalDateTime start,
                            @RequestParam(value = "end",required = false)LocalDateTime end ) {
        model.addAttribute("promotions", service.getAll(status,start,end));
        model.addAttribute("user", UserUltil.getUser());
        return "admin/khuyenmai";
    }

    @GetMapping("promotion/new")
    public String formAdd(Model model) {
        model.addAttribute("promotion", new PromotionDto());
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("products", productService.getProductNoPromotion());
        return "admin/add-khuyenmai";
    }

    @PostMapping("/promotion/add")
    public String addPromition(@Valid @ModelAttribute("promotion") PromotionDto dto, Model model,
                               BindingResult result,
                               @RequestParam(value = "sps", required = false) List<Long> list) {
        if (result.hasErrors()) {
            model.addAttribute("product", productService.getProductNoPromotion());
            model.addAttribute("user", UserUltil.getUser());
            return "admin/promotion/promotion";
        }
        service.add(dto.promotion(), list);
        return "redirect:/admin/promotion";
    }

    @GetMapping("/promotion/{id}")
    public String promotionDetail(@PathVariable Long id, Model model) {
        PromotionDto promotion = new PromotionDto(service.detail(id));
        model.addAttribute("promotion", promotion);
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("products", productService.getProductNoPromotion());
        model.addAttribute("product", promotion.getProducts());
        model.addAttribute("categorys", categoryService.getAll());
        model.addAttribute("types", typeProductService.getAll());
        return "admin/update-khuyenmai";
    }

    @PostMapping("/promotion/{id}")
    public String update(@ModelAttribute("promotion") PromotionDto dto) {
        service.update(dto);
        return "redirect:/admin/promotion";
    }

    @GetMapping("/promotion/{id}/delete-product/{sp}")
    public String deleteProduct(@PathVariable("id") Long id, @PathVariable("sp") Long sp) {
        service.deleteProduct(sp);
        return "redirect:/admin/promotion/" + id;
    }

    @GetMapping("/promotion/delete/{id}")
    public String deletePromotion(@PathVariable("id") Long id) {
        service.delete(id);
        return "redirect:/admin/promotion";
    }

    @PostMapping("/promotion/add-product/{id}")
    public String addProduct(@PathVariable("id") Long id,
                             @RequestParam(value = "category", required = false) Category category,
                             @RequestParam(value = "type", required = false) TypeProduct type,
                             @RequestParam(value = "products", required = false) List<Long> products) {
        productService.updatePromotion(id,category,type,products);
        return "redirect:/admin/promotion/" + id;
    }

}