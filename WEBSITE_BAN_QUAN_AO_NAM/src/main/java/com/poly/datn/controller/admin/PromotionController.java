package com.poly.datn.controller.admin;

import com.poly.datn.dto.CategoryDto;
import com.poly.datn.dto.ProductDto;
import com.poly.datn.dto.PromotionDto;
import com.poly.datn.dto.TtpeProductDto;
import com.poly.datn.entity.Category;
import com.poly.datn.entity.Promotion;
import com.poly.datn.service.CategoryService;
import com.poly.datn.service.ProductService;
import com.poly.datn.service.PromotionService;
import com.poly.datn.service.TypeProductService;
import com.poly.datn.util.UserUltil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class PromotionController {
    @Autowired
    private PromotionService service;

    @Autowired
    private ProductService productService;

    @Autowired
    private TypeProductService typeProductService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/promotion")
    public String promition(@RequestParam(defaultValue = "0", name = "page") Integer pageNum, Model model) {
//        Page<Promotion> page = service.phanTrang(pageNum, 5);
        model.addAttribute("list", service.getAll());
//        model.addAttribute("list", service.getAll());
        model.addAttribute("object", new Promotion());
        model.addAttribute("user", UserUltil.getUser());
        return "admin/promotion/promotion";
    }

    @PostMapping("/promotion/add")
    public String addPromition(@Valid @ModelAttribute("object") PromotionDto dto, Model model, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("user", UserUltil.getUser());
            return "admin/promotion/promotion";
        }
        service.add(dto);
        return "redirect:/admin/promotion";
    }

    @GetMapping("/promotion/{id}")
    public String promotionDetail(@PathVariable Long id, Model model) {
        Promotion promotion = service.detail(id);
        model.addAttribute("list", promotion);
        model.addAttribute("user", UserUltil.getUser());
        return "/admin/promotion/promotion-update";
    }


    @PostMapping("/promotion/update/{id}")
    public String updateProduct(@ModelAttribute("list") PromotionDto dto, @PathVariable("id") Long id) {
        service.updateP(dto, id);
        return "redirect:/admin/promotion";
    }

    @GetMapping("/promotion/update/{id}")
    public String newProduct(Model model, @PathVariable Long id) {

        Promotion promotion = service.detail(id);
        model.addAttribute("l", new PromotionDto(promotion));
        model.addAttribute("object", new TtpeProductDto());


        model.addAttribute("ob", new CategoryDto());

        model.addAttribute("list", productService.getAll());
        model.addAttribute("types", typeProductService.getAll());

        model.addAttribute("category", categoryService.fillAll());
        model.addAttribute("user", UserUltil.getUser());
        return "admin/promotion/promotion-add-product";
    }

    @PostMapping("/promotion/update/product/{id}")
    public String addPr(@RequestParam("product") List<Long> list, @PathVariable Long id) {
        service.addProductd(list, id);

        return "redirect:/admin/promotion";

    }


}