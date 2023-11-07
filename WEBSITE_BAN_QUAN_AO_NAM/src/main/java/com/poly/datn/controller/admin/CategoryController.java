package com.poly.datn.controller.admin;

import com.poly.datn.entity.Category;
import com.poly.datn.service.CategoryService;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("admin")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("category")
    public String hienthi(Model model) {
        model.addAttribute("list", service.getAll());
        model.addAttribute("object", new Category());
        return "admin/category";
    }
    @GetMapping("category/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        model.addAttribute("list", service.getAll());
        model.addAttribute("object", service.detail(id));
        return "admin/category";
    }
    @PostMapping("category/save")
    public String save(RedirectAttributes attributes, @ModelAttribute("object") Category category){
        MessageUtil messageUtil = service.save(category);
        attributes.addFlashAttribute("message",messageUtil);
        return "redirect:/admin/category";
    }

    @ModelAttribute("message")
    public MessageUtil initMessage() {
        return new MessageUtil();
    }

    @ModelAttribute("user")
    public Object initUser() {
        return UserUltil.getUser();
    }
}
