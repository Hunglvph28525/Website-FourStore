package com.poly.datn.controller.admin;

import com.poly.datn.entity.Category;
import com.poly.datn.entity.Color;
import com.poly.datn.entity.Description;
import com.poly.datn.service.DescriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mo-ta")
public class DescriptionsController {
    @Autowired
    DescriptionService descriptionService;

    @GetMapping("/hien-thi")
    public String hienThi(@RequestParam(defaultValue = "0", name = "page") Integer pageNum, Model model) {
        Page<Description> page = descriptionService.phanTrang(pageNum, 1);
        model.addAttribute("list", page);
        model.addAttribute("att", new Description());
        return "description/index";
    }

    @GetMapping("/view-add")
    public  String viewAdd(Model model){
        model.addAttribute("att", new Description());
        return "description/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("att") Description description, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "description/index";
        }

        description.setManual(description.getManual());
        description.setFabric(description.getFabric());
        description.setDescriptionProduct(description.getDescriptionProduct());
        description.setStyle(description.getStyle());
        description.setPattern(description.getPattern());
        model.addAttribute("att", description);
        descriptionService.add(description);
        return "redirect:/mo-ta/hien-thi";
    }

    @GetMapping("view-update/{id}")
    public String viewUpdate(@PathVariable Long id, Model model) {
        Description description = descriptionService.detail(id);
        model.addAttribute("att", description);
        return "description/update";
    }

    @PostMapping("update")
    public String update(@Valid @ModelAttribute("att") Description description, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "description/update";
        }
        description.setManual(description.getManual());
        description.setFabric(description.getFabric());
        description.setDescriptionProduct(description.getDescriptionProduct());
        description.setStyle(description.getStyle());
        description.setPattern(description.getPattern());
        model.addAttribute("att", description);
        descriptionService.add(description);
        return "redirect:/mo-ta/hien-thi";
    }
}
