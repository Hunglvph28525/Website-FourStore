package com.poly.datn.controller.admin;


import com.poly.datn.entity.TypeProduct;
import com.poly.datn.service.CategoryService;
import com.poly.datn.service.TypeProductService;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class TypeProductController {
    @Autowired
    private TypeProductService service;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/product-type")
    public String show(Model model) {
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("list", service.getAllDto());
        model.addAttribute("productType", new TypeProduct());
        model.addAttribute("categorys", categoryService.fillAll());
        return "admin/product-type/type";
    }

    @PostMapping("/product-type/form")
    public String save(@ModelAttribute("ProductType") TypeProduct typeProduct) {
        service.save(typeProduct);
        return "redirect:/admin/product-type";
    }

    @GetMapping("/product-type/{id}")
    public String detail(@ModelAttribute("color") TypeProduct dto, @PathVariable("id") Long id, Model model) {
        model.addAttribute("list", service.getAllDto());
        model.addAttribute("productType", service.detail(id));
        model.addAttribute("categorys", categoryService.fillAll());
        model.addAttribute("user", UserUltil.getUser());
        return "admin/product-type/type";
    }

}
