package com.poly.datn.controller.admin;

import com.poly.datn.dto.CategoryDto;
import com.poly.datn.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public String show(Model model){
        model.addAttribute("list", categoryService.getAll());
        model.addAttribute("formadd",new CategoryDto());
        return "admin/category/category";
    }
}
