package com.poly.datn.controller;

import com.poly.datn.entity.Category;
import com.poly.datn.entity.Color;
import com.poly.datn.service.ColorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mau-sac")
public class ColorController {
    @Autowired
    ColorService colorService;

    @GetMapping("/hien-thi")
    public String hienThi(@RequestParam(defaultValue = "0", name = "page") Integer pageNum, Model model) {
        Page<Color> page = colorService.phanTrang(pageNum, 1);
        model.addAttribute("list", page);
        model.addAttribute("att", new Color());
        return "color/index";
    }

    @GetMapping("/view-add")
    public  String viewAdd(Model model){
        model.addAttribute("att", new Color());
        return "color/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("att") Color color, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "color/index";
        }

        color.setStatus("1");
        color.setColorName(color.getColorName());
        model.addAttribute("att", color);
        colorService.add(color);
        return "redirect:/mau-sac/hien-thi";
    }

    @GetMapping("view-update/{id}")
    public String viewUpdate(@PathVariable Integer id, Model model) {
        Color color = colorService.detail(id);
        model.addAttribute("att", color);
        return "color/update";
    }

    @PostMapping("update")
    public String update(@Valid @ModelAttribute("att") Color color, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "color/update";
        }
        color.setColorName(color.getColorName());
        color.setStatus(color.getStatus());
        colorService.add(color);
        return "redirect:/mau-sac/hien-thi";
    }

}
