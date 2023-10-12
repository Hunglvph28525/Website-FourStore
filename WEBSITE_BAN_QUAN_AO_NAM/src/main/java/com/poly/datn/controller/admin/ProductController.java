package com.poly.datn.controller.admin;

import com.poly.datn.entity.Category;
import com.poly.datn.entity.Color;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.Promotion;
import com.poly.datn.service.CategoryService;
import com.poly.datn.service.ColorService;
import com.poly.datn.service.ProductService;
import com.poly.datn.service.PromotionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/san-pham")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    PromotionService promotionService;

    @Autowired
    ColorService colorService;


    @GetMapping("/hien-thi")
    public String hienThi(@RequestParam(defaultValue = "0", name = "page") Integer pageNum, Model model) {
        Page<Product> page = productService.phanTrang(pageNum, 1);
        model.addAttribute("list", page);
        model.addAttribute("att", new Product());
        return "product/index";
    }

    @GetMapping("view-add")
    public String viewAdd(Model model) {
        model.addAttribute("listTheLoai", categoryService.getAll());
        model.addAttribute("listKhuyenMai",promotionService.getAll());
        model.addAttribute("att", new Product());
        model.addAttribute("tl",new Category());
        model.addAttribute("km",new Promotion());
        return "product/add";
    }

    @PostMapping("add")
    public String add(@Valid @ModelAttribute("att") Product product,
                      BindingResult result,
                      RedirectAttributes redirectAttributes,
                      Model model, HttpSession session) {

        if (result.hasErrors()) {
            model.addAttribute("listTheLoai", categoryService.getAll());
            model.addAttribute("listKhuyenMai", promotionService.getAll());
            model.addAttribute("att", new Category());
            model.addAttribute("km",new Promotion());
            return "product/add";
        }

        product.setProductName(product.getProductName());
        product.setCreateDate(product.getCreateDate());
        product.setCategory(product.getCategory());
        product.setPromotion(product.getPromotion());
        product.setPrice(product.getPrice());
        product.setPriceImport(product.getPriceImport());
        product.setStatus("1");
        product.setDefaultImage("123");
        productService.add(product);
        return "redirect:/san-pham/hien-thi";
    }




}
