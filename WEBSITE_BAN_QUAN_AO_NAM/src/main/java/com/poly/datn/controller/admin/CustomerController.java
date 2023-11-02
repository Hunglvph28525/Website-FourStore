package com.poly.datn.controller.admin;

import com.poly.datn.entity.User;
import com.poly.datn.repository.RoleRepository;
import com.poly.datn.service.ImageService;
import com.poly.datn.service.UserService;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class CustomerController {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private ImageService imageService;

    @GetMapping("/customer")
    public String hienThi(Model model, @RequestParam(value = "status", defaultValue = "0") String status) {
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("listKH", userService.getAll(status));
        return "admin/khachhang";
    }

    @GetMapping("/view-add/customer")
    public  String add(Model model){
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("att", new User());
        return "admin/add-khach-hang";
    }


    @PostMapping("/customer/add")
    public String add(@ModelAttribute("att") User user, BindingResult result,@RequestParam("file") MultipartFile file) throws IOException {

        if (result.hasErrors()) {
            return "admin/customer";
        }
        Map<?, ?> map = imageService.upload(file, "other");
        user.setUsername(user.getUsername());
        user.setName(user.getName());
        user.setPhoneNumber(user.getPhoneNumber());
        user.setPassword(user.getPassword());
        user.setEmail(user.getEmail());
        user.setGender(user.getGender());
        user.setAvatar(map.get("url").toString());
        user.setStatus("onKH");
        user.setRoles(Collections.singletonList(roleRepository.getByName("ROLE_USER")));
        userService.add(user);
        return "redirect:/admin/customer";

    }

    @GetMapping("customer/view-update/{id}")
    public String viewUpdate(@PathVariable Long id, Model model) {
        model.addAttribute("listanh", imageService.getListanh(id));
        model.addAttribute("user", UserUltil.getUser());
        User user = userService.detailCustomer(id);
        model.addAttribute("att", user);
        return "admin/update-khach-hang";
    }


    @PostMapping("/customer/update")
    public String update(@ModelAttribute("att") User user, BindingResult result,@RequestParam("file") MultipartFile file) throws IOException {

        if (result.hasErrors()) {
            return "admin/update-khach-hang";
        }
        Map<?, ?> map = imageService.upload(file, "other");
        user.setUsername(user.getUsername());
        user.setName(user.getName());
        user.setPhoneNumber(user.getPhoneNumber());
        user.setPassword(user.getPassword());
        user.setEmail(user.getEmail());
        user.setGender(user.getGender());
        user.setAvatar(map.get("url").toString());
        user.setStatus(user.getStatus());
        user.setRoles(Collections.singletonList(roleRepository.getByName("ROLE_USER")));
        userService.add(user);
        return "redirect:/admin/customer";

    }








}
