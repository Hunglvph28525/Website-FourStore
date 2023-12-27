package com.poly.datn.controller.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.datn.dto.GioHangDto;
import com.poly.datn.dto.SelectedProduct;
import com.poly.datn.entity.Product;
import com.poly.datn.service.BrandService;
import com.poly.datn.service.CartService;
import com.poly.datn.service.ProductService;
import com.poly.datn.service.PromotionService;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class WebController {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PromotionService promotionService;

    @GetMapping()
    public String homeLocal(Model model){
        model.addAttribute("topProducts", productService.getAll("0",0L,0L,0L));
        model.addAttribute("brands", brandService.getAll());
//        model.addAttribute("giohang", cartService.getGioHang());
        return "web/index";
    }
    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("topProducts", productService.getAll("0",0L,0L,0L));
        model.addAttribute("brands", brandService.getAll());
//        model.addAttribute("giohang", cartService.getGioHang());
        return "web/index";
    }
    @GetMapping("/shop")
    public String shop(Model model){
        model.addAttribute("topProducts", productService.getAll("0",0L,0L,0L));
//        model.addAttribute("brands", brandService.getAll());
//        model.addAttribute("giohang", cartService.getGioHang());
        return "web/sanpham";
    }
    @GetMapping("/shop/{id}")
    public String detail(Model model, @PathVariable("id") Long id){
        model.addAttribute("product", productService.getDetail(id));
//        GioHangDto gioHangDto = cartService.getGioHang();
//        System.out.println(gioHangDto.toString());
//        model.addAttribute("giohang", gioHangDto);
        model.addAttribute("topProducts", productService.getAll("0",0L,0L,0L));
        model.addAttribute("typeProduct",productService.getAll("0",0L,0L,0L));
        return "web/sanphamchitiet";
    }
    @GetMapping("/cart")
    public String cart(){
        return "web/cart";
    }


    @PostMapping("/checkout")
    public String processOrder(@RequestParam("selectedProducts") String selectedProductsJson, Model model) throws JsonProcessingException {
        List<SelectedProduct> selectedProducts = objectMapper.readValue(selectedProductsJson, new TypeReference<List<SelectedProduct>>() {});
        MessageUtil messageUtil = cartService.createOrder(selectedProducts);
        model.addAttribute("order", messageUtil.getObject());
        return "web/checkout"; // Hoặc trả về trang cảm ơn
    }

    @ModelAttribute("giohang")
    public Object initGiohang(){
        return cartService.getGioHang();
    }
    @ModelAttribute("message")
    public MessageUtil initMessage() {
        return new MessageUtil();
    }
    @ModelAttribute("pgg")
    public Object initpgg() {
        return promotionService.getAll("1");
    }
}
