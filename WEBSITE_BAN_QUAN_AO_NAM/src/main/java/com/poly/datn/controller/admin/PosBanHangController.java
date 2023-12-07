package com.poly.datn.controller.admin;

import com.poly.datn.dto.ProductDetailDto;
import com.poly.datn.dto.UserDto;
import com.poly.datn.entity.User;
import com.poly.datn.service.*;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class PosBanHangController {
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ProductDetailService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/sale")
    public String sale(Model model) {
        model.addAttribute("invoice", invoiceService.fillAll("0"));

        return "admin/banhang";
    }

    @GetMapping("/sale/create-invoice")
    public String create(RedirectAttributes attributes) {
        attributes.addFlashAttribute("message", invoiceService.create());
        return "redirect:/admin/sale";
    }

    @GetMapping("/cart/{code}/{sp}")
    public String deleteSp(@PathVariable("code") String code, @PathVariable("sp") Long sp, RedirectAttributes attributes) {
        attributes.addFlashAttribute("message", invoiceService.deleteSp(code, sp));
        return "redirect:/admin/sale";
    }

    @GetMapping("sale/{code}/add-kh/{kh}")
    public String addKh(@PathVariable("code") String code, @PathVariable("kh") Long kh, RedirectAttributes attributes) {
        MessageUtil messager = invoiceService.addUser(code, kh);
        attributes.addFlashAttribute("message", messager);
        return "redirect:/admin/sale";
    }

    @GetMapping("sale/{code}/add-pgg/{pgg}")
    public String addpgg(@PathVariable("code") String code, @PathVariable("pgg") Long pgg, RedirectAttributes attributes) {
        MessageUtil messager = invoiceService.addUPgg(code, pgg);
        attributes.addFlashAttribute("message", messager);
        return "redirect:/admin/sale";
    }

    @GetMapping("sale/{code}/delete-pgg")
    public String addpgg(@PathVariable("code") String code, RedirectAttributes attributes) {
        MessageUtil messager = invoiceService.deletePgg(code);
        attributes.addFlashAttribute("message", messager);
        return "redirect:/admin/sale";
    }

    @PostMapping("sale/invoice/payment/{codeBill}")
    public String thanhToan(@PathVariable("codeBill") String codeBill,
                            @RequestParam("paymentMethod") Integer paymentMethod,
                            HttpServletRequest request,
                            @RequestParam(value = "tienkhachdua", required = false) Double tienKhachDua,
                            RedirectAttributes attributes) throws UnsupportedEncodingException {

        if (paymentMethod == 2) {
            String url = paymentService.paymentOnlineOnCounter(codeBill, paymentMethod, request);
            return "redirect:" + url;
        } else {
            MessageUtil messageUtil = invoiceService.payment(codeBill, paymentMethod, tienKhachDua);
            attributes.addFlashAttribute("message", messageUtil);
            return "redirect:/admin/sale";
        }

    }

    @PostMapping("/sale/new-kh/{codeBill}")
    public String newK(RedirectAttributes attributes, @ModelAttribute("addUser") User user, @PathVariable("codeBill") String codeBill) {

        MessageUtil messageUtil = invoiceService.newUser(user, codeBill);
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/admin/sale";
    }

    @GetMapping("/paymetsucset")
    public String paymentRestpon(
            @RequestParam("vnp_OrderInfo") String ghiChu,
            @RequestParam("vnp_TxnRef") String codeBill,
            @RequestParam("vnp_PayDate") String payDate,
            @RequestParam("vnp_ResponseCode") String status,
            RedirectAttributes attributes) {
        MessageUtil messageUtil = invoiceService.resposePayment(codeBill, ghiChu, payDate, status);
        attributes.addFlashAttribute("message", messageUtil);

        return "redirect:/admin/sale";
    }

    @GetMapping("/sale/{codeBill}/add-{sp}")
    public String addToCart(@PathVariable("codeBill") String code, @PathVariable("sp") Long sp, RedirectAttributes attributes) {
        attributes.addFlashAttribute("message", invoiceService.addProduct(code, sp));
        return "redirect:/admin/sale";
    }

    @PostMapping("/sale/addShipping/{codeBill}")
    public String addShipping(@PathVariable("codeBill") String codeBill,
                              @RequestParam("name") String name,
                              @RequestParam("sdt") String sdt,
                              @RequestParam("city") String city,
                              @RequestParam("district") String district,
                              @RequestParam("ward") String ward,
                              @RequestParam("diachi") String diaChi,
                              RedirectAttributes attributes) {

        MessageUtil message = invoiceService.addShipping(codeBill, city, district, ward, diaChi, sdt, name);
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/sale";
    }

    @GetMapping("/sale/{codeBill}/huy")
    public String huyGH(@PathVariable("codeBill") String codeBill,RedirectAttributes attributes) {
        MessageUtil message = invoiceService.huyGh(codeBill);
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/sale";
    }

    @ModelAttribute("addUser")
    public Object initAddUser() {
        return new User();
    }

    @ModelAttribute("user")
    public Object initUser() {
        return UserUltil.getUser();
    }

    @ModelAttribute("message")
    public MessageUtil initMessage() {
        return new MessageUtil();
    }

    @ModelAttribute("products")
    public List<ProductDetailDto> iniProducts() {
        return productService.getAll();
    }

    @ModelAttribute("kh")
    public List<UserDto> initKh() {
        return userService.getAll("0");
    }

    @ModelAttribute("pgg")
    public Object initpgg() {
        return promotionService.getAll("1");
    }
}
