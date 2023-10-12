package com.poly.datn.controller.admin;

import com.poly.datn.entity.Category;
import com.poly.datn.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Controller
@RequestMapping("/the-loai")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @GetMapping("/hien-thi")
    public String hienThi(@RequestParam(defaultValue = "0", name = "page") Integer pageNum, Model model) {
        Page<Category> page = categoryService.phanTrang(pageNum, 1);
        model.addAttribute("list", page);
        model.addAttribute("att", new Category());
        return "category/index";
    }

    @GetMapping("/view-add")
    public  String viewAdd(Model model){
        model.addAttribute("att", new Category());
        return "category/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("att") Category category, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "category/index";
        }

        category.setStatus("1");
        category.setCategoryName(category.getCategoryName());
        model.addAttribute("att", category);
        categoryService.add(category);
        return "redirect:/the-loai/hien-thi";
    }

    @GetMapping("view-update/{id}")
    public String viewUpdate(@PathVariable Long id, Model model) {
        Category category = categoryService.detail(id);
        model.addAttribute("att", category);
        return "category/update";
    }

    @PostMapping("update")
    public String update(@Valid @ModelAttribute("att") Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "category/update";
        }
        category.setCategoryName(category.getCategoryName());
        category.setStatus(category.getStatus());
        categoryService.add(category);
        return "redirect:/the-loai/hien-thi";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        categoryService.delete(id);
        return "redirect:/the-loai/hien-thi";
    }






}
