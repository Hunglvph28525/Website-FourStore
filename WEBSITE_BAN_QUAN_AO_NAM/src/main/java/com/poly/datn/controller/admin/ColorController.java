package com.poly.datn.controller.admin;


import com.poly.datn.entity.Color;
import com.poly.datn.service.ColorService;
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
public class ColorController {
    @Autowired
    private ColorService service;

    @GetMapping("/color")
    public String show(Model model){
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("list", service.getAll());
        model.addAttribute("color",new Color());
        return "admin/color/color";
    }
    @PostMapping("/color/form")
    public String save(@ModelAttribute("color") Color color){
        service.save(color);
        return "redirect:/admin/color";
    }

    @GetMapping("/color/{id}")
    public String detail(@ModelAttribute("color") Color dto, @PathVariable("id") Integer id, Model model){
        model.addAttribute("list", service.getAll());
        model.addAttribute("color",service.detail(id));
        model.addAttribute("user", UserUltil.getUser());
        return "admin/color/color";
    }
}
