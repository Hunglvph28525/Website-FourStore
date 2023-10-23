package com.poly.datn.controller.admin;

import com.poly.datn.dto.CategoryDto;
import com.poly.datn.service.CategoryService;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @GetMapping("/category")
    public String show(Model model){
        model.addAttribute("user", UserUltil.getUser());
        List<CategoryDto> list = categoryService.getAll();
        model.addAttribute("list", list);
        model.addAttribute("category",new CategoryDto());
        return "admin/category/category";
    }
    @PostMapping("/category/form")
    public String save(@ModelAttribute("category") CategoryDto dto){
        categoryService.Save(dto);
        return "redirect:/admin/category";
    }

    @GetMapping("/category/{id}")
    public String detail(@ModelAttribute("category") CategoryDto dto, @PathVariable("id") Long id, Model model){
        model.addAttribute("list", categoryService.getAll());
        model.addAttribute("category",categoryService.detail(id));
        model.addAttribute("user", UserUltil.getUser());
        return "admin/category/category";
    }

    @GetMapping("category/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        categoryService.delete(id);
        return "redirect:/admin/category";
    }
}
