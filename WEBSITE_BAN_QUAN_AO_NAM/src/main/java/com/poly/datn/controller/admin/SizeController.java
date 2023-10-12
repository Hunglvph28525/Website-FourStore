package com.poly.datn.controller.admin;

import com.poly.datn.entity.Category;
import com.poly.datn.entity.Size;
import com.poly.datn.service.SizeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/size")
public class SizeController {
    @Autowired
    SizeService sizeService;

    @GetMapping("/hien-thi")
    public String hienThi(@RequestParam(defaultValue = "0", name = "page") Integer pageNum, Model model) {
        Page<Size> page = sizeService.phanTrang(pageNum, 1);
        model.addAttribute("list", page);
        model.addAttribute("att", new Category());
        return "size/index";
    }

    @GetMapping("/view-add")
    public  String viewAdd(Model model){
        model.addAttribute("att", new Size());
        return "size/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("att") Size size, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "size/index";
        }

        size.setStatus("1");
        size.setName(size.getName());
        model.addAttribute("att", size);
        sizeService.add(size);
        return "redirect:/size/hien-thi";
    }

    @GetMapping("view-update/{id}")
    public String viewUpdate(@PathVariable Integer id, Model model) {
        Size size = sizeService.detail(id);
        model.addAttribute("att", size);
        return "size/update";
    }

    @PostMapping("update")
    public String update(@Valid @ModelAttribute("att") Size size, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "size/update";
        }
        size.setName(size.getName());
        size.setStatus(size.getStatus());
        sizeService.add(size);
        return "redirect:/size/hien-thi";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        sizeService.delete(id);
        return "redirect:/size/hien-thi";
    }
}
