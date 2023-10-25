package com.poly.datn.controller.admin;


import com.poly.datn.entity.Size;
import com.poly.datn.service.SizeService;
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
public class SizeController {

    @Autowired
    private SizeService sizeService;
    @GetMapping("/size")
    public String show(Model model){
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("list", sizeService.getAll());
        model.addAttribute("size",new Size());
        return "admin/size/size";
    }
    @PostMapping("/size/form")
    public String save(@ModelAttribute("size") Size size){
        sizeService.save(size);
        return "redirect:/admin/size";
    }

    @GetMapping("/size/{id}")
    public String detail(@ModelAttribute("size") Size dto, @PathVariable("id") Integer id, Model model){
        model.addAttribute("list", sizeService.getAll());
        model.addAttribute("size",sizeService.detail(id));
        model.addAttribute("user", UserUltil.getUser());
        return "admin/size/size";
    }

}
