package com.poly.datn.controller.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.datn.dto.OrderDetailDto;
import com.poly.datn.dto.ProductDto;
import com.poly.datn.dto.SelectedProduct;
import com.poly.datn.entity.Address;
import com.poly.datn.request.UserSignUpRequest;
import com.poly.datn.service.*;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
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
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private TypeProductService typeProductService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @Autowired
    private OderService oderService;

    private OrderDetailDto detailDto = null;

    @GetMapping()
    public String homeLocal(Model model) {
        model.addAttribute("topProducts", productService.getAllWebSPmoi());
        return "web/index";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("topProducts", productService.getAllWebSPmoi());
        return "web/index";
    }

    @GetMapping("/shop")
    public String shop(Model model,
                       @RequestParam(value = "category",defaultValue = "0") Long type,
                       @RequestParam(value = "brand", defaultValue = "0") Long brand) {
       List<ProductDto>  x = productService.getAllWebLoc(type,brand);
        model.addAttribute("topProducts", x);
        return "web/sanpham";
    }

    @GetMapping("/shop/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        model.addAttribute("product", productService.getDetail(id));
        model.addAttribute("typeProduct", productService.getAllWebSPmoi());
        return "web/sanphamchitiet";
    }

    @GetMapping("/cart")
    public String cart() {
        return "web/cart";
    }


    @PostMapping("/checkout")
    public String processOrder(@RequestParam("selectedProducts") String selectedProductsJson, Model model) throws JsonProcessingException {
        List<SelectedProduct> selectedProducts = objectMapper.readValue(selectedProductsJson, new TypeReference<List<SelectedProduct>>() {
        });
        MessageUtil messageUtil = cartService.createOrder(selectedProducts);
        this.detailDto = (OrderDetailDto) messageUtil.getObject();
        model.addAttribute("order", messageUtil.getObject());
        return "web/checkout"; // Hoặc trả về trang cảm ơn
    }

    @GetMapping("/checkout")
    public String getOrder(Model model) {
        if (this.detailDto == null) {
            return "redirect:/cart";
        }
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
            RedirectAttributes attributes) {
        MessageUtil messageUtil = cartService.newAddressOrder(this.detailDto, name, sdt, province, district, ward, street);
        this.detailDto = (OrderDetailDto) messageUtil.getObject();
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/checkout";
    }

    @PostMapping("/checkout/edit-address")
    public String editAddress(@RequestParam("diachi") Long idAddress, RedirectAttributes attributes) {
        MessageUtil messageUtil = cartService.editAddressOrder(this.detailDto, idAddress);
        this.detailDto = (OrderDetailDto) messageUtil.getObject();
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/checkout";
    }

    @GetMapping("cart/delete-product/{id}")
    public String deleteproduct(@PathVariable("id") Long id, Model model) {
        MessageUtil message = cartService.deleteProduct(id);
        model.addAttribute("message", message);
        return "redirect:/cart";
    }

    @GetMapping("/checkout/add-pgg/{id}")
    public String addPgg(@PathVariable("id") Long idPgg, RedirectAttributes attributes) {
        MessageUtil messageUtil = cartService.addPgg(this.detailDto, idPgg);
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
        MessageUtil messageUtil = oderService.resposePayment(codeBill, ghiChu, payDate, status);
        if (messageUtil.getStatus() == 0) {
            return "redirect:/checkout";
        } else {
            this.detailDto = (OrderDetailDto) messageUtil.getObject();
            return "redirect:/thankyou";
        }

    }

    @PostMapping("/order/payment")
    public String paymentOrder(@RequestParam("note") String note,
                               @RequestParam("paymethod") Integer payId,
                               HttpServletRequest request) throws UnsupportedEncodingException {
        return oderService.paymentOrder(note, payId, this.detailDto, request);
    }

    @GetMapping("thankyou")
    public String thankYou() {
        return "web/thankyou";
    }

    @GetMapping("/lien-he")
    public String lienhe() {
        return "web/lienhe";
    }

    @GetMapping("/bai-viet")
    public String blog() {
        return "web/blog";
    }

    @PostMapping("/delete/cart")
    public String deleteCart(@RequestParam("selectedProducts") String selectedProductsJson, RedirectAttributes model) throws JsonProcessingException {
        List<SelectedProduct> selectedProducts = objectMapper.readValue(selectedProductsJson, new TypeReference<List<SelectedProduct>>() {
        });
        MessageUtil messageUtil = cartService.deleteCart(selectedProducts);
        model.addFlashAttribute("message", messageUtil);
        return "redirect:/cart"; // Hoặc trả về trang cảm ơn
    }

    @GetMapping("/tai-khoan")
    public String acount(Model model) {
        model.addAttribute("address", addressService.getAddressForCustomer());
        model.addAttribute("invoice", invoiceService.getInvoceByUser());
        return "web/my-account";
    }

    @GetMapping("/invoice/{codeBill}")
    public String invoicedetai(@PathVariable("codeBill") String codeBill, Model model) {
        model.addAttribute("detail", invoiceService.detail(codeBill));
        model.addAttribute("transaction", transactionService.getAll(codeBill));
        return "web/invoice-detail";
    }

    @PostMapping("/tai-khoan/add-address")
    public String addDresss(@RequestParam("district") String district,
                            @RequestParam("ward") String ward,
                            @RequestParam("street") String street,
                            @RequestParam("province") String province,
                            @RequestParam("status") String status,
                            RedirectAttributes attributes) {

        MessageUtil messageUtil = addressService.address(Address.builder()
                .district(district)
                .ward(ward)
                .street(street)
                .province(province)
                .status(status)
                .build());
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/tai-khoan";
    }

    @GetMapping("/tai-khoan/delete-address/{id}")
    public String deleteAddress(@PathVariable("id") Long id, RedirectAttributes attributes) {
        MessageUtil messageUtil = addressService.delete(id);
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/tai-khoan";
    }

    @PostMapping("/invoice/huy/{codeBill}")
    public String huyDh(@PathVariable("codeBill") String codeBill, @RequestParam("ghichu") String note, RedirectAttributes attributes) {
        MessageUtil messageUtil = invoiceService.huyDh(codeBill, note);
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/tai-khoan";
    }

    @PostMapping("/tai-khoan/doi-thong-tin")
    public String doiThongTin(@RequestParam("password") String pass, RedirectAttributes attributes) {
        MessageUtil messageUtil = userService.doiThongTin(pass);
        attributes.addFlashAttribute("message", messageUtil);
        return "redirect:/tai-khoan";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "web/login";
    }


    @PostMapping("/sign-up")
    public String dang(Model model, RedirectAttributes attributes, @ModelAttribute @Valid UserSignUpRequest userSignUpRequest,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "web/login";
        }
        return userService.dangKy(model, userSignUpRequest, attributes);
    }
    @PostMapping("/fogot-password")
    public String fogotPass(@RequestParam("email") String email, RedirectAttributes attributes){
        return userService.fogotPass(email,attributes);
    }
    @GetMapping("/fogot-password")
    public String fogotPass(){
        return "web/fogot-password";
    }

    @ModelAttribute("giohang")
    public Object initGiohang() {
        return cartService.getGioHang();
    }

    @ModelAttribute("user")
    public Object initUser() {
        return UserUltil.getUser();
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

    @ModelAttribute("category")
    public Object initType() {
        return categoryService.getAll();
    }
    @ModelAttribute("typeProduct")
    public Object iniTypeProduct(){
        return typeProductService.getAll();
    }

    @ModelAttribute("userSignUpRequest")
    public Object initDangki() {
        return new UserSignUpRequest();
    }

    @ModelAttribute("brands")
    public Object initbrand() {
        return brandService.getAll();
    }
}
