package com.poly.datn.controller.admin;

import com.poly.datn.entity.Color;
import com.poly.datn.service.ColorService;
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
@RequestMapping("/admin")
public class ColorController {

    @Autowired
    private ColorService service;


    @GetMapping("color")
    public String hienthi(Model model) {
        model.addAttribute("list", service.getAll());
        model.addAttribute("object", new Color());
        return "admin/color";
    }
    @GetMapping("color/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("list", service.getAll());
        model.addAttribute("object", service.detail(id));
        return "admin/color";
    }
    @PostMapping("color/save")
    public String save(RedirectAttributes attributes, @ModelAttribute("object") Color color){
        MessageUtil messageUtil = service.save(color);
        attributes.addFlashAttribute("message",messageUtil);
        return "redirect:/admin/color";
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
