package com.poly.datn.controller.admin;

import com.poly.datn.dto.UserRequest;
import com.poly.datn.entity.Address;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.User;
import com.poly.datn.repository.AddressRepository;
import com.poly.datn.repository.RoleRepository;
import com.poly.datn.repository.UserRepository;
import com.poly.datn.service.AddressService;
import com.poly.datn.service.ImageService;
import com.poly.datn.service.UserService;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class CustomerController {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private ImageService imageService;


    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    UserRepository userRepository;


//    private  static  final Logger log = LoggerFactory.getLogger(CustomerController.class);


    @GetMapping("/customer")
    public String hienThi(Model model, @RequestParam(value = "status", defaultValue = "0") String status) {
//        model.addAttribute("user", UserUltil.getUser());
//        model.addAttribute("listKH", userService.getAll(status));

//        System.out.println(userService.findByIdDiaChi(2L));


        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("listKH", userService.getAll(status));
        return "admin/khachhang";
    }

    @GetMapping("/tk/{id}")
    public String tk(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", UserUltil.getUser());
        List<Address> list = userService.findByIdDiaChi(id);
        model.addAttribute("items", list);
        model.addAttribute("att", new Address());

        User user = userService.detailCustomer(id);
        model.addAttribute("user", user);


        return "admin/diachi-khachhang";
    }

    //add dia chi
    @PostMapping("/add/address")
    public String addAddress(Model model, @ModelAttribute("att") Address address, @ModelAttribute("user") User user) {
        model.addAttribute("user", UserUltil.getUser());
        address.setDistrict(address.getDistrict());
        address.setProvince(address.getProvince());
        address.setWard(address.getWard());
        address.setStreet(address.getStreet());
        address.setStatus("off");
        address.setUser(user);
        addressService.add(address);
//        return "admin/diachi-khachhang";
        return "redirect:/admin/tk/" + user.getId();

    }

    //cap nhat dia chi mac dinh


    @GetMapping("/view-add/customer")
    public String add(Model model) {
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("att", new UserRequest());
        return "admin/add-khach-hang";
    }


    @PostMapping("/customer/add")
    public String add(@Valid @ModelAttribute("att") UserRequest user, BindingResult result, @RequestParam("file") MultipartFile file, RedirectAttributes attributes,Model model) throws IOException {
        if (result.hasErrors()) {
            return "admin/khachhang";
        }

        MessageUtil message = userService.add(user, file);
        attributes.addFlashAttribute("message", message);
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
    public String update(@ModelAttribute("att") User user, BindingResult result, @RequestParam("file") MultipartFile file) throws IOException {

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
        user.setAddresses(user.getAddresses());

        userService.add(user);

        return "redirect:/admin/customer";

    }


}
