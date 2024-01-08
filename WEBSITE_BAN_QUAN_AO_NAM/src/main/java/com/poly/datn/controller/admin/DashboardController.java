package com.poly.datn.controller.admin;

import com.poly.datn.repository.InvoiceRepository;
import com.poly.datn.repository.ProductDetailRepository;
import com.poly.datn.repository.UserRepository;
import com.poly.datn.service.InvoiceService;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes("user")
@RequestMapping("/admin")
@Controller
public class DashboardController {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    InvoiceRepository invoiceRepository;
    @ModelAttribute("user")
    public Object initUser() {
        return UserUltil.getUser();
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("tongkhachhang",userRepository.getKhachHang());
        model.addAttribute("tongsanpham",productDetailRepository.getTongSoSP());
        model.addAttribute("tonghoadon",invoiceRepository.getHoaDon());
        model.addAttribute("spsaphet",productDetailRepository.getSPSapHet());
        model.addAttribute("newKH",userRepository.getNewKH());
        model.addAttribute("sphet",productDetailRepository.getSpSapHet());

        model.addAttribute("all", invoiceService.hoaDonMoi());

        return "admin/index";
    }
}
