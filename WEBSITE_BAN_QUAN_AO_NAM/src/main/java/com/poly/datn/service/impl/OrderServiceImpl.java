package com.poly.datn.service.impl;

import com.poly.datn.dto.OrderDetailDto;
import com.poly.datn.entity.*;
import com.poly.datn.entity.composite.CartId;
import com.poly.datn.entity.composite.InvoiceId;
import com.poly.datn.repository.*;
import com.poly.datn.service.CartService;
import com.poly.datn.service.OderService;
import com.poly.datn.service.PaymentService;
import com.poly.datn.service.UserService;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OderService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceDetailRepository detailRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;




    @Override
    public String paymentOrder(String note, Integer payId, OrderDetailDto detailDto, HttpServletRequest reg) throws UnsupportedEncodingException {
        User user = UserUltil.getUser();
        if (user == null) return "/login";
        Invoice invoice = Invoice.builder()
                .user(UserUltil.getUser())
                .promotion(detailDto.getPromotion())
                .total(BigDecimal.valueOf(detailDto.getTotal()))
                .giaGiam(BigDecimal.valueOf(detailDto.getGiaGiam()))
                .shipping(detailDto.getShipping())
                .shippingCost(BigDecimal.valueOf(detailDto.getShippingCost()))
                .grandTotal(BigDecimal.valueOf(detailDto.getGrandTotal()))
                .sdtNhan(detailDto.getSdt())
                .createDate(LocalDateTime.now())
                .codeBill("HD" + generateCodeBill(5))
                .note(note)
                .status("1")
                .type("0")
                .build();
        invoice.setShippingAddress(ShippingAddress.builder()
                .invoice(invoice)
                .street(detailDto.getAddress().getStreet())
                .ward(detailDto.getAddress().getWard())
                .district(detailDto.getAddress().getDistrict())
                .province(detailDto.getAddress().getProvince())
                .build());
        invoice.setPaymentMethod(paymentMethodRepository.getReferenceById(payId));
        Invoice temp = invoiceRepository.save(invoice);
        Transaction transaction = Transaction.builder()
                .invoice(invoice)
                .status(invoice.getStatus())
                .createDate(invoice.getCreateDate())
                .name(UserUltil.getUser().getName())
                .note(note)
                .build();
        transactionRepository.save(transaction);
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        List<ProductDetail> productDetails = new ArrayList<>();
        detailDto.getProducts().stream().forEach(x -> {
            ProductDetail productDetai = productDetailRepository.getReferenceById(x.getProduct().getId());
            productDetai.setQuantity(productDetai.getQuantity() - x.getQuantity());
            productDetails.add(productDetai);
            invoiceDetails.add(InvoiceDetail.builder()
                    .invoiceId(new InvoiceId(productDetai, temp))
                    .price(BigDecimal.valueOf(x.getPrice()))
                    .quantity(x.getQuantity())
                    .build());
        });
        if (detailDto.getPromotion() != null){
            Promotion promotion = detailDto.getPromotion();
            promotion.setQuantity(promotion.getQuantity() -1);
            promotionRepository.save(promotion);
        }
        Cart cart = cartRepository.getByUser_Id(user.getId());
        productDetails.stream().forEach(x -> {
            CartId cartId = new CartId(x,cart);
            cartDetailRepository.deleteById(cartId);
        });
        productDetailRepository.saveAll(productDetails);
        detailRepository.saveAll(invoiceDetails);
        if (payId == 1) {
            temp.setPaymentInfo("Nhận hàng thanh toán Ship COD");
            temp.setPaymentStatus("0");
            temp.setUserPay(BigDecimal.valueOf(0));
            invoiceRepository.save(temp);
            return "redirect:/thankyou";
        } else {
            return "redirect:" + paymentService.paymentOnlineOnWeb(temp,reg);
        }
    }

    @Override
    public MessageUtil resposePayment(String codeBill, String ghiChu, String payDate, String status) {
        Invoice invoice = invoiceRepository.getReferenceById(codeBill);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime datePay = LocalDateTime.parse(payDate, formatter);
        if (status.equals("00")) {
            invoice.setPaymentDate(datePay);
            invoice.setPaymentInfo(ghiChu + "thanh cong !");
            invoice.setPaymentStatus("1");
            invoice.setShippingDate(LocalDateTime.now());
            invoiceRepository.save(invoice);
            return MessageUtil.builder().status(1).object(new OrderDetailDto()).build();
        } else {
//            invoice.setPaymentDate(datePay);
            invoice.setPaymentInfo(ghiChu + "Thanh toán thất bại");
            invoice.setPaymentStatus("0");
//            invoice.setShippingDate(LocalDateTime.now());
            invoiceRepository.save(invoice);
            return MessageUtil.builder().message("Áp dụng phiếu giảm giá thành công !").status(0).type("bg-success").build();
        }
    }

    private String generateCodeBill(int x) {
        String characters = "0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < x; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }
}
