package com.poly.datn.controller.admin;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.Promotion;
import com.poly.datn.service.ProductService;
import com.poly.datn.service.PromotionService;
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

import java.util.List;


@Controller
@RequestMapping("/admin")
public class PromotionController {
    @Autowired
    private PromotionService service;

    @Autowired
    private ProductService productService;

    @GetMapping("/promotion")
    public String promition( Model model) {
        model.addAttribute("list", service.getAll());
        model.addAttribute("product", productService.getProductNoPromotion());
        model.addAttribute("object", new Promotion());
        model.addAttribute("user", UserUltil.getUser());
        return "admin/promotion/promotion";
    }

    @PostMapping("/promotion/add")
    public String addPromition(@Valid @ModelAttribute("object") PromotionDto dto, Model model, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("product", productService.getProductNoPromotion());
            model.addAttribute("user", UserUltil.getUser());
            return "admin/promotion/promotion";
        }
        service.add(dto);
        return "redirect:/admin/promotion";
    }

    @GetMapping("/promotion/{id}")
    public String promotionDetail(@PathVariable Long id, Model model) {
        PromotionDto promotion = new PromotionDto(service.detail(id));
        model.addAttribute("promotion", promotion);
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("product", productService.getProductNoPromotion());
        return "/admin/promotion/promotion-update";
    }


    @PostMapping("/promotion/update/{id}")
    public String updateProduct(@ModelAttribute("list") PromotionDto dto, @PathVariable("id") Long id) {
        service.updateP(dto, id);
        return "redirect:/admin/promotion";
    }

    @GetMapping("/promotion/{id}/delete/{sp}")
    public String newProduct(Model model, @PathVariable Long id, @PathVariable Long sp) {
        Product product = productService.detail(sp);
        product.setPromotion(null);
//        productService.save(product);
        return "redirect:/admin/promotion/" + id;
    }

    @GetMapping("/promotion/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        service.delete(id);
        return "redirect:/admin/promotion";
    }


    @PostMapping("/promotion/add-product/{id}")
    public String addPr(@RequestParam(value = "product",required = false) List<Product> products, @PathVariable Long id) {
        service.addProduct(products, id);
        return "redirect:/admin/promotion/"+id;
    }


}