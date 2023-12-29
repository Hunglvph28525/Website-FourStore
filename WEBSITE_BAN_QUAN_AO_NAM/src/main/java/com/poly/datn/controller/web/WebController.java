package com.poly.datn.controller.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.datn.dto.GioHangDto;
import com.poly.datn.dto.OrderDetailDto;
import com.poly.datn.dto.SelectedProduct;
import com.poly.datn.entity.Product;
import com.poly.datn.service.*;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OderService oderService;

    private OrderDetailDto detailDto = null;

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
        this.detailDto = (OrderDetailDto) messageUtil.getObject();
        model.addAttribute("order", messageUtil.getObject());
        return "web/checkout"; // Hoặc trả về trang cảm ơn
    }

    @GetMapping("/checkout")
    public String getOrder(Model model){
        if (this.detailDto == null ){ return "redirect:/cart";}
        model.addAttribute("order", this.detailDto);
        return "web/checkout";
    }
    @PostMapping("/checkout/add-address")
    public String newAddress(
            @RequestParam("name") String name,
            @RequestParam("sdt") String sdt,
            @RequestParam("district") String district,
            @RequestParam("ward") String ward,
            @RequestParam("street") String street,
            @RequestParam("province") String province,
            RedirectAttributes attributes){
        MessageUtil messageUtil = cartService.newAddressOrder(this.detailDto,name,sdt,province,district,ward,street);
        this.detailDto = (OrderDetailDto) messageUtil.getObject();
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/checkout";
    }
    @PostMapping("/checkout/edit-address")
    public String editAddress(@RequestParam("diachi") Long idAddress, RedirectAttributes attributes){
        MessageUtil messageUtil = cartService.editAddressOrder(this.detailDto,idAddress);
        this.detailDto = (OrderDetailDto) messageUtil.getObject();
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/checkout";
    }

    @GetMapping("cart/delete-product/{id}")
    public String deleteproduct(@PathVariable("id") Long id, Model model){
        MessageUtil message = cartService.deleteProduct(id);
        model.addAttribute("message", message);
        return "redirect:/cart";
    }
    @GetMapping("/checkout/add-pgg/{id}")
    public String addPgg(@PathVariable("id") Long idPgg, RedirectAttributes attributes){
        MessageUtil messageUtil = cartService.addPgg(this.detailDto,idPgg);
        this.detailDto = (OrderDetailDto) messageUtil.getObject();
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/checkout";

    }
    @GetMapping("/paymetsucset")
    public String paymentRestpon(
            @RequestParam("vnp_OrderInfo") String ghiChu,
            @RequestParam("vnp_TxnRef") String codeBill,
            @RequestParam("vnp_PayDate") String payDate,
            @RequestParam("vnp_ResponseCode") String status,
            RedirectAttributes attributes) {
        MessageUtil messageUtil  = oderService.resposePayment(codeBill, ghiChu, payDate, status);
        if (messageUtil.getStatus() == 0) {
            return "redirect:/checkout";
        }else {
            this.detailDto = (OrderDetailDto) messageUtil.getObject();
            return "redirect:/thankyou";
        }

    }
    @PostMapping("/order/payment")
    public String paymentOrder(@RequestParam("note") String note,
                               @RequestParam("paymethod") Integer payId,
                               HttpServletRequest request) throws UnsupportedEncodingException {
        return  oderService.paymentOrder(note,payId, this.detailDto,request);
    }
    @GetMapping("thankyou")
    public String thankYou(){
        return "web/thankyou";
    }
    @PostMapping("/delete/cart")
    public String deleteCart(@RequestParam("selectedProducts") String selectedProductsJson, RedirectAttributes  model) throws JsonProcessingException {
        List<SelectedProduct> selectedProducts = objectMapper.readValue(selectedProductsJson, new TypeReference<List<SelectedProduct>>() {});
        MessageUtil messageUtil = cartService.deleteCart(selectedProducts);
        model.addFlashAttribute("message", messageUtil);
        return "redirect:/cart"; // Hoặc trả về trang cảm ơn
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
    @ModelAttribute("paymeThod")
    public Object iniPaymetod() {
        return paymentService.getPaymentMethods();
    }
}
