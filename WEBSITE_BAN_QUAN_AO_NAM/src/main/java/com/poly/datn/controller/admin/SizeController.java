package com.poly.datn.controller.admin;


import com.poly.datn.entity.Size;
import com.poly.datn.service.SizeService;
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
public class SizeController {
    @Autowired
    private SizeService service;

    @GetMapping("size")
    public String hienthi(Model model) {
        model.addAttribute("list", service.getAll());
        model.addAttribute("object", new Size());
        return "admin/size";
    }
    @GetMapping("size/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("list", service.getAll());
        model.addAttribute("object", service.detail(id));
        return "admin/size";
    }
    @PostMapping("size/save")
    public String save(RedirectAttributes attributes,@ModelAttribute("object") Size size){
        MessageUtil messageUtil = service.save(size);
        attributes.addFlashAttribute("message",messageUtil);
        return "redirect:/admin/size";
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
