package com.poly.datn.controller.admin;

import com.poly.datn.dto.PromotionDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;


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
    public String add(@Valid @ModelAttribute("att") UserRequest user, BindingResult result, @RequestParam(value = "file",required = false) MultipartFile file, RedirectAttributes attributes, Model model) throws IOException {
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
    public String update(@ModelAttribute("att") User user, BindingResult result, @RequestParam(value = "file",required = false) MultipartFile file, RedirectAttributes attributes) throws IOException {




        String oldEmail = userService.detailCustomer(user.getId()).getEmail();
        System.out.println("EMAIL CŨ CỦA NGƯỜI DÙNG TRƯỚC: " + oldEmail);

        String matKhauCu = userService.detailCustomer(user.getId()).getPassword();
        String userold = userService.detailCustomer(user.getId()).getUsername();

        Random random = new Random();
        int number;
        String mk = "";
        for (int i = 0; i < 5; i++) {
            number = random.nextInt(9);
            mk += number;
        }
        String matKhau = "abcd" + mk;


        String s1 = user.getEmail();
        String[] parts = s1.split("@");
        String part1 = parts[0];


        user.setName(user.getName());
        user.setPhoneNumber(user.getPhoneNumber());


        user.setEmail(s1);
        user.setGender(user.getGender());
        user.setStatus(user.getStatus());
        user.setRoles(Collections.singletonList(roleRepository.getByName("ROLE_USER")));
        user.setAddresses(user.getAddresses());
        user.setPassword(matKhauCu);
        user.setUsername(userold);

//        if(user.getStatus().equals("onKH")){
//            user.setPassword(matKhauCu);
//            user.setUsername(userold);
//        }
//
//
//        if(user.getStatus().equals("offKH")){
//            user.setPassword(matKhauCu);
//            user.setUsername(userold);
//        }





        MessageUtil message = userService.updateCustomer(user);
        attributes.addFlashAttribute("message", message);

        System.out.println("EMAIL MỚI:" + s1);


        return "redirect:/admin/customer";

    }



    // dia chi detail
    @GetMapping("address/detail/{id}")
    public String addressDetail(@PathVariable Long id, Model model) {
        model.addAttribute("user", UserUltil.getUser());
        Address address = userService.detailAddress(id);
        System.out.println(address.getUser().getId());

        model.addAttribute("att", address);
        return "admin/update-diachi";
    }

    //update dia chi

    @PostMapping("/address/update")
    public String updateAddress(@ModelAttribute("att") Address address, BindingResult result) throws IOException {

        if (result.hasErrors()) {
            return "admin/update-diachi";
        }

        address.setStreet(address.getStreet());
        address.setStatus(address.getStatus());
        address.setWard(address.getWard());
        address.setProvince(address.getProvince());
        address.setUser(address.getUser());
        address.setStatus("off");
        addressService.add(address);
//        return "redirect:/admin/customer";
        return "redirect:/admin/tk/" + address.getUser().getId();

    }

    @GetMapping("/account/{user}/dc-macdinh/{id}")
    public String diaChiMacDinh(@PathVariable("id") Long idAddress, @PathVariable("user") Long idUser) {
        MessageUtil messageUtil = addressService.selectMacDinh(idUser, idAddress);
        return "redirect:/admin/tk/" + idUser;
    }

    @ModelAttribute("message")
    public Object initmesage() {
        return new MessageUtil();
    }
}
