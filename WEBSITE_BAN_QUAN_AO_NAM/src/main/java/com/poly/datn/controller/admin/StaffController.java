package com.poly.datn.controller.admin;

import com.poly.datn.dto.UserRequest;
import com.poly.datn.entity.User;
import com.poly.datn.repository.RoleRepository;
import com.poly.datn.service.ImageService;
import com.poly.datn.service.UserService;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class StaffController {
    @Autowired
    UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    RoleRepository roleRepository;


    @GetMapping("/staff")
    public String hienThi(Model model, @RequestParam(value = "status", defaultValue = "0") String status) {
//        model.addAttribute("user", UserUltil.getUser());
//        model.addAttribute("listKH", userService.getAll(status));

//        System.out.println(userService.findByIdDiaChi(2L));


        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("listNV", userService.getAllStaff());
        return "admin/nhanvien";
    }

    @GetMapping("/view-add/staff")
    public String add(Model model) {
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("att", new UserRequest());
        return "admin/add-nhan-vien";
    }

    @PostMapping("/staff/add")
    public String add(@Valid @ModelAttribute("att") UserRequest user, BindingResult result, @RequestParam("file") MultipartFile file, RedirectAttributes attributes, Model model) throws IOException {
        if (result.hasErrors()) {
            return "admin/nhanvien";
        }

        MessageUtil message = userService.addStaff(user, file);
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/staff";

    }

    @GetMapping("staff/view-update/{id}")
    public String viewUpdate(@PathVariable Long id, Model model) {
        model.addAttribute("listanh", imageService.getListanh(id));
        model.addAttribute("user", UserUltil.getUser());
        User user = userService.detailStaff(id);
        model.addAttribute("att", user);
        return "admin/update-nhan-vien";
    }

    @PostMapping("/staff/update")
    public String update(@ModelAttribute("att") User user, BindingResult result, @RequestParam("file") MultipartFile file) throws IOException {

        if (result.hasErrors()) {
            return "admin/update-nhan-vien";
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
        user.setRoles(Collections.singletonList(roleRepository.getByName("ROLE_NV")));
        user.setAddresses(user.getAddresses());

        userService.add(user);

        return "redirect:/admin/staff";


    }

    @ModelAttribute("message")
    public Object initmesage() {
        return new MessageUtil();
    }

}
