package com.poly.datn.controller.admin;

import com.poly.datn.dto.ProductDto;
import com.poly.datn.entity.Invoice;
import com.poly.datn.service.InvoiceService;
import com.poly.datn.service.ProductService;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class PosBanHangController {
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ProductService productService;

    @GetMapping("/sale")
    public String sale(Model model){
        model.addAttribute("invoice",invoiceService.fillAll("0"));
        return "admin/banhang";
    }

    @GetMapping("/sale/create-invoice")
    public String create(RedirectAttributes attributes){
        attributes.addFlashAttribute("message", invoiceService.create());
     return "redirect:/admin/sale";
    }


    @ModelAttribute("user")
    public Object initUser(){
        return UserUltil.getUser();
    }
    @ModelAttribute("message")
    public MessageUtil initMessage() {
        return new MessageUtil();
    }
    @ModelAttribute("products")
    public List<ProductDto> iniProducts() {
        return productService.getAll("0",0l,0l,0l);
    }
}
