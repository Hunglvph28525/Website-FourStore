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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/admin")
public class StaffController {
    @Autowired
    UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JavaMailSender javaMailSender;


    @GetMapping("/staff")
    public String hienThi(Model model, @RequestParam(value = "status", defaultValue = "0") String status) {
//        model.addAttribute("user", UserUltil.getUser());
//        model.addAttribute("listKH", userService.getAll(status));

//        System.out.println(userService.findByIdDiaChi(2L));


        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("listNV", userService.getAllNV(status));
        return "admin/nhanvien";
    }

    @GetMapping("/view-add/staff")
    public String add(Model model) {
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("att", new UserRequest());
        return "admin/add-nhan-vien";
    }

    @PostMapping("/staff/add")
    public String add(@Valid @ModelAttribute("att") UserRequest user, BindingResult result, @RequestParam(value = "file",required = false) MultipartFile file, RedirectAttributes attributes, Model model) throws IOException {
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
    public String update(@ModelAttribute("att") User user, BindingResult result, @RequestParam(value = "file",required = false) MultipartFile file,RedirectAttributes attributes) throws IOException {
        String oldEmail = userService.detailCustomer(user.getId()).getEmail();
        System.out.println("EMAIL CŨ CỦA NGƯỜI DÙNG TRƯỚC: " + oldEmail);

        String matKhauCu = userService.detailCustomer(user.getId()).getPassword();

        String userold = userService.detailCustomer(user.getId()).getUsername();
        if (result.hasErrors()) {
            return "admin/update-nhan-vien";
        }

        Random random = new Random();
        int number;
        String mk = "";
        for (int i = 0; i < 5; i++) {
            number = random.nextInt(9);
            mk += number;
        }
        String matKhau = "abcd" + mk;


//        Map<?, ?> map = imageService.upload(file, "other");
        String s1 = user.getEmail();
        String[] parts = s1.split("@");
        String part1 = parts[0];
//        user.setUsername(part1);

        user.setName(user.getName());
        user.setPhoneNumber(user.getPhoneNumber());
        user.setEmail(user.getEmail());
        user.setGender(user.getGender());
//        user.setAvatar(map.get("url").toString());
        user.setStatus(user.getStatus());
        user.setRoles(Collections.singletonList(roleRepository.getByName("ROLE_USER")));
        user.setAddresses(user.getAddresses());

//        if(!user.getEmail().equals(oldEmail) && user.getStatus().equals("onKH")){
//            user.setPassword(matKhau);
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setTo(user.getEmail());
//            mailMessage.setSubject("THÔNG TIN ĐĂNG NHẬP CỦA BẠN");
//            mailMessage.setText("Tên đăng nhập:" + part1 + "\nMật khẩu đăng nhập:" + matKhau);
//            javaMailSender.send(mailMessage);
//        }
        if(user.getEmail().equals(oldEmail) && user.getStatus().equals("onKH")){
            user.setPassword(matKhauCu);
            user.setUsername(userold);
        }
        if(user.getStatus().equals("offKH")){
            user.setPassword(matKhauCu);
            user.setUsername(userold);
        }

        MessageUtil message = userService.updateCustomer(user);
        attributes.addFlashAttribute("message", message);

        return "redirect:/admin/staff";


    }

    @ModelAttribute("message")
    public Object initmesage() {
        return new MessageUtil();
    }

}
