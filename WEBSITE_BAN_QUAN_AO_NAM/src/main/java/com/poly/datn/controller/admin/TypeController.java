package com.poly.datn.controller.admin;

import com.poly.datn.entity.TypeProduct;
import com.poly.datn.service.CategoryService;
import com.poly.datn.service.TypeProductService;
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

import java.io.IOException;

@Controller
@RequestMapping("admin")
public class TypeController {
    @Autowired
    private TypeProductService service;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("type")
    public String hienthi(Model model) {
        model.addAttribute("list", service.getAll());
        model.addAttribute("object", new TypeProduct());
        return "admin/type";
    }

    @GetMapping("type/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        model.addAttribute("list", service.getAll());
        model.addAttribute("object", service.detail(id));
        return "admin/type";
    }

    @PostMapping("type/save")
    public String save(RedirectAttributes attributes, @ModelAttribute("object") TypeProduct x) throws IOException {
        MessageUtil messageUtil = service.save(x);
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/admin/type";
    }

    @ModelAttribute("message")
    public MessageUtil initMessage() {
        return new MessageUtil();
    }

    @ModelAttribute("user")
    public Object initUser() {
        return UserUltil.getUser();
    }
    @ModelAttribute("categorys")
    public Object initCategory(){
        return categoryService.getAll();
    }

}
