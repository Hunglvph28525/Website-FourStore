package com.poly.datn.service.impl;

import com.poly.datn.dto.InvoiceDetailDto;
import com.poly.datn.dto.InvoiceDto;
import com.poly.datn.dto.UpdateQuantityReq;
import com.poly.datn.entity.*;
import com.poly.datn.entity.composite.InvoiceId;
import com.poly.datn.repository.*;
import com.poly.datn.service.InvoiceService;
import com.poly.datn.service.UserService;
import com.poly.datn.util.Fomater;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository repository;

    @Autowired
    private InvoiceDetailRepository detailRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<InvoiceDto> getAll() {
        List<Invoice> list = repository.getAll();
        List<InvoiceDto> invoiceDtos = new ArrayList<>();
        list.stream().forEach(x -> {
            if (x.getUser() == null) {
                x.setUser(User.builder().name("Khách lẻ").build());
            }
            invoiceDtos.add(new InvoiceDto(x));
        });
        return invoiceDtos;
    }

    @Override
    public List<InvoiceDto> fillAll(String status) {
        List<InvoiceDto> list = repository.fillAll(status);
        list.stream().forEach(o -> {
            List<InvoiceDetailDto> products = detailRepository.getAllInvoi(o.getCodeBill());
            o.setProduct(products == null ? products = new ArrayList<>() : products);
            o.setTotal(o.getProduct().stream().map(x -> x.getPrice() * x.getQuantity()).reduce(0, Integer::sum));
            o.setTongSp(products.stream().count());
            o.setTotalFomat(Fomater.fomatTien().format(o.getTotal()));
            o.setGiaGiamFomat(Fomater.fomatTien().format(o.getGiaGiam()));
            o.setShippingCostFomat(Fomater.fomatTien().format(o.getShippingCost()));
            o.setGrandTotalFomat(Fomater.fomatTien().format(o.getGrandTotal()));
        });
        return list;
    }

    @Override
    public List<Invoice> getStatus(String status) {
        List<Invoice> list = repository.getStatus(status);
        list.stream().forEach(x -> {
            if (x.getUser() == null) {
                x.setUser(User.builder().name("Khách lẻ").build());
            }
        });
        return list;


    }

    @Override
    public InvoiceDto detail(String codeBill) {
        InvoiceDto invoiceDto = new InvoiceDto(repository.getReferenceById(codeBill));
        invoiceDto.setProduct(detailRepository.getAllInvoi(codeBill));
        return invoiceDto;
    }

    @Override
    public MessageUtil create() {
        if (repository.countAllByStatus("0") < 5) {
            Invoice invoice = Invoice.builder()
                    .codeBill("HD" + generateCodeBill(5))
                    .status("0")
                    .type("1")
                    .note("Đơn hàng được tạo bởi " + UserUltil.getUser().getName())
                    .createDate(LocalDateTime.now())
                    .total(BigDecimal.valueOf(0))
                    .shippingCost(BigDecimal.valueOf(0))
                    .giaGiam(BigDecimal.valueOf(0))
                    .grandTotal(BigDecimal.valueOf(0))
                    .shipping(false)
                    .tienThoi(BigDecimal.ZERO)
                    .userPay(BigDecimal.ZERO)
                    .build();
            invoice = repository.save(invoice);
            Transaction transaction = Transaction.builder().invoice(invoice)
                    .name(UserUltil.getUser().getName())
                    .status(invoice.getStatus())
                    .note(invoice.getNote())
                    .createDate(LocalDateTime.now())
                    .build();
            transactionRepository.save(transaction);
            return new MessageUtil(1, "Tạo đơn hàng thành công", "bg-success", invoice);
        }


        return new MessageUtil(0, "Thất bại tạo tối đa 5 đơn hàng", "bg-danger");
    }

    @Override
    public MessageUtil delete(String codeBill) {
        Invoice invoice = repository.getReferenceById(codeBill);
        List<InvoiceDetail> list = detailRepository.getByCodeBill(codeBill);
        detailRepository.deleteAll(list);
        List<Transaction> transactions = invoice.getTransactions().stream().toList();
        transactionRepository.deleteAll(transactions);
        repository.deleteById(codeBill);
        return new MessageUtil(1, "Xóa thành công", "bg-success");
    }

    @Override
    public MessageUtil addProduct(String codeBill, Long sp) {
        ProductDetail productDetail = productDetailRepository.getReferenceById(sp);
        Invoice invoice = repository.getReferenceById(codeBill);
        if (detailRepository.existsByInvoiceId(new InvoiceId(productDetail, invoice))) {
            InvoiceDetail detail = detailRepository.getReferenceById(new InvoiceId(productDetail, invoice));
            detail.setQuantity(detail.getQuantity() + 1);
            detailRepository.save(detail);
            repository.save(invoice);

        } else {
            InvoiceDetail detail = new InvoiceDetail(new InvoiceId(productDetail, invoice), 1, productDetail.getPrice());
            productDetail.setQuantity(productDetail.getQuantity() - 1);
            productDetailRepository.save(productDetail);
            detailRepository.save(detail);

        }
        List<InvoiceDetail> list = detailRepository.getByCodeBill(codeBill);
        BigDecimal total = list.stream().map(x -> x.getPrice().multiply(BigDecimal.valueOf(x.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        invoice = repository.getReferenceById(codeBill);
        Promotion promotion = invoice.getPromotion();
        if (promotion != null) {
            promotion.setQuantity(promotion.getQuantity() + 1);
            promotionRepository.save(promotion);
        }
        invoice.setTotal(total);
        invoice.setPromotion(null);
        invoice.setGiaGiam(BigDecimal.ZERO);
        invoice.setGrandTotal(total);
        invoice.setGrandTotal(invoice.getGrandTotal().add(invoice.getShippingCost()));
        repository.save(invoice);
        return new MessageUtil(1, "Thêm thành công", "bg-success");
    }

    @Override
    public MessageUtil deleteSp(String code, Long sp) {
        Invoice invoice = repository.getReferenceById(code);
        ProductDetail productDetail = productDetailRepository.getReferenceById(sp);
        Promotion promotion = invoice.getPromotion();
        if (promotion != null) {
            promotion.setQuantity(promotion.getQuantity() + 1);
            promotionRepository.save(promotion);
        }
        InvoiceDetail detail = detailRepository.getReferenceById(new InvoiceId(productDetail, invoice));
        detailRepository.delete(detail);
        productDetail.setQuantity(productDetail.getQuantity() + detail.getQuantity());
        productDetailRepository.save(productDetail);
        List<InvoiceDetail> list = detailRepository.getByCodeBill(code);
        BigDecimal total = list.stream().map(x -> x.getPrice().multiply(BigDecimal.valueOf(x.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        invoice = repository.getReferenceById(code);
        invoice.setTotal(total);
        invoice.setPromotion(null);
        invoice.setGiaGiam(BigDecimal.ZERO);
        invoice.setGrandTotal(total);
        invoice.setGrandTotal(invoice.getGrandTotal().add(invoice.getShippingCost()));
        repository.save(invoice);

        return new MessageUtil(1, "Xóa thành công", "bg-success");
    }

    @Override
    public Map<?, ?> updateSL(UpdateQuantityReq req) {
        ProductDetail productDetail = productDetailRepository.getReferenceById(req.getProductId());
        Invoice invoice = repository.getReferenceById(req.getCodeBill());
        Promotion promotion = invoice.getPromotion();
        if (promotion != null) {
            promotion.setQuantity(promotion.getQuantity() + 1);
            promotionRepository.save(promotion);
        }
        InvoiceDetail detail = detailRepository.getReferenceById(new InvoiceId(productDetail, invoice));
        productDetail.setQuantity(productDetail.getQuantity() + detail.getQuantity());
        detail.setQuantity(req.getNewQuantity());
        productDetail.setQuantity(productDetail.getQuantity() - detail.getQuantity());
        productDetailRepository.save(productDetail);
        List<InvoiceDetail> list = detailRepository.getByCodeBill(req.getCodeBill());
        BigDecimal total = list.stream().map(x -> x.getPrice().multiply(BigDecimal.valueOf(x.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        invoice.setTotal(total);
        invoice.setPromotion(null);
        invoice.setGiaGiam(invoice.getGiaGiam());
        invoice.setGrandTotal(total);
        invoice.setGrandTotal(invoice.getGrandTotal().add(invoice.getShippingCost()));
        invoice = repository.save(invoice);
        Map<String, Integer> map = new HashMap<>();
        map.put("total", invoice.getTotal().intValue());
        map.put("giaGiam", invoice.getGiaGiam().intValue());
        map.put("phiShip", invoice.getShippingCost().intValue());
        map.put("grandTotal", invoice.getGrandTotal().intValue());
        System.out.println(map);
        return map;
    }

    @Override
    public MessageUtil addUser(String codeBill, Long user) {
        Invoice invoice = repository.getReferenceById(codeBill);
        if (user != -1) {
            User user1 = userRepository.getReferenceById(user);
            invoice.setUser(user1);
        } else {
            invoice.setUser(null);
        }

        repository.save(invoice);
        return new MessageUtil(1, "Thêm khách hàng thành công", "bg-success");
    }

    @Override
    public MessageUtil addUPgg(String code, Long pgg) {
        Invoice invoice = repository.getReferenceById(code);
        Promotion promotion = invoice.getPromotion();
        if (promotion != null) {
            promotion.setQuantity(promotion.getQuantity() + 1);
            promotionRepository.save(promotion);
        }
        promotion = promotionRepository.getReferenceById(pgg);
        System.out.println(promotion);
        if (invoice.getTotal().doubleValue() >= promotion.getMinValue().doubleValue() && promotion.getQuantity() > 0) {
            invoice.setGiaGiam(invoice.getTotal().multiply(BigDecimal.valueOf(promotion.getDiscountValue()).divide(new BigDecimal("100"))));
            invoice.setGrandTotal(invoice.getTotal().subtract(invoice.getGiaGiam()));
            invoice.setPromotion(promotion);
            promotion.setQuantity(promotion.getQuantity() - 1);
            invoice.setGrandTotal(invoice.getGrandTotal().add(invoice.getShippingCost()));
            promotionRepository.save(promotion);
            repository.save(invoice);
            return new MessageUtil(1, "Áp dụng thành công", "bg-success");
        }
        return new MessageUtil(0, "Thất bại vì đơn không đủ điều kiện", "bg-danger");
    }

    @Override
    public MessageUtil deletePgg(String code) {
        Invoice invoice = repository.getReferenceById(code);
        Promotion promotion = invoice.getPromotion();
        if (promotion != null) {
            invoice.setPromotion(null);
            invoice.setGiaGiam(BigDecimal.ZERO);
            invoice.setGrandTotal(invoice.getTotal());
            promotion.setQuantity(promotion.getQuantity() + 1);
            invoice.setGrandTotal(invoice.getGrandTotal().add(invoice.getShippingCost()));
            promotionRepository.save(promotion);
            repository.save(invoice);
        }
        return new MessageUtil(1, "Xóa thành công", "bg-success");
    }

    @Override
    public MessageUtil payment(String codeBill, Integer paymentMethod, Double tienKhachDua) {
        Invoice invoice = repository.getReferenceById(codeBill);
        invoice.setPaymentMethod(paymentMethodRepository.getReferenceById(paymentMethod));
        if (tienKhachDua >= invoice.getGrandTotal().doubleValue()) {
            invoice.setPaymentDate(LocalDateTime.now());
            if (invoice.getShipping()) {
                invoice.setStatus("2");
            } else {
                invoice.setStatus("4");
            }
            invoice.setPaymentInfo("Đơn hàng được thanh toán bằng Tiền mặt thành công !");
            invoice.setPaymentStatus("1");
            invoice.setUserPay(BigDecimal.valueOf(tienKhachDua));
            invoice.setTienThoi(invoice.getUserPay().subtract(invoice.getGrandTotal()));
            invoice.setShippingDate(LocalDateTime.now());
            invoice = repository.save(invoice);
            Transaction transaction = Transaction.builder()
                    .invoice(invoice)
                    .createDate(LocalDateTime.now())
                    .note("đơn hàng được thanh toán bằng tiền mặt")
                    .status(invoice.getStatus())
                    .name(UserUltil.getUser().getName())
                    .build();
            transactionRepository.save(transaction);

            return MessageUtil.builder().message("Thanh toán thành công !").status(1).type("bg-success").build();
        } else {
            return MessageUtil.builder().message("Thanh toán không thành công !").status(1).type("bg-danger").build();
        }


    }

    @Override
    public MessageUtil resposePayment(String codeBill, String ghiChu, String payDate, String status) {
        Invoice invoice = repository.getReferenceById(codeBill);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime datePay = LocalDateTime.parse(payDate, formatter);
        if (status.equals("00")) {
            invoice.setPaymentDate(datePay);
            if (invoice.getShipping()) {
                invoice.setStatus("2");
            } else {
                invoice.setStatus("4");
            }
            invoice.setPaymentInfo(ghiChu + "thanh cong !");
            invoice.setPaymentStatus("1");
            invoice.setShippingDate(LocalDateTime.now());
            invoice = repository.save(invoice);
            Transaction transaction = Transaction.builder()
                    .invoice(invoice)
                    .createDate(LocalDateTime.now())
                    .note("đơn hàng được thanh toán bằng VN Pay")
                    .status(invoice.getStatus())
                    .name(UserUltil.getUser().getName())
                    .build();
            transactionRepository.save(transaction);

            return MessageUtil.builder().message("Thanh toán thành công !").status(1).type("bg-success").build();
        } else {
            return MessageUtil.builder().message("Đã xảy ra Lỗi khi thanh toán. code = " + status).status(0).type("bg-danger").build();
        }
    }

    @Override
    public MessageUtil newUser(User user, String codeBill) {
        user = userService.addUserToBill(user);
        Invoice invoice = repository.getReferenceById(codeBill);
        invoice.setUser(user);
        repository.save(invoice);
        return MessageUtil.builder().message("Thêm khách hàng thành công !").status(1).type("bg-success").build();
    }

    @Override
    public MessageUtil addShipping(String codeBill, String city, String district, String ward, String diaChi, String sdt, String name) {
        Invoice invoice = repository.getReferenceById(codeBill);
        BigDecimal shipCost = BigDecimal.ZERO;
        if (city.equals("Thành phố Hà Nội")) {
            shipCost = BigDecimal.valueOf(15000);
        } else {
            shipCost = BigDecimal.valueOf(25000);
        }
        ShippingAddress shippingAddress = new ShippingAddress();
        invoice.setShipping(true);
        if (invoice.getShippingAddress() == null) {
            invoice.setSdtNhan(sdt);
            invoice.setShippingAddress(ShippingAddress.builder()
                    .province(city)
                    .district(district)
                    .ward(ward)
                    .street(diaChi)
                    .invoice(invoice)
                    .build());
            invoice.setShippingCost(shipCost);
            invoice.setGrandTotal(invoice.getGrandTotal().add(invoice.getShippingCost()));
        } else {
            shippingAddress = invoice.getShippingAddress();
            invoice.setGrandTotal(invoice.getGrandTotal().subtract(invoice.getShippingCost()));
            invoice.setSdtNhan(sdt);
            invoice.setShippingAddress(ShippingAddress.builder()
                    .province(city)
                    .district(district)
                    .ward(ward)
                    .street(diaChi)
                    .invoice(invoice)
                    .build());
            invoice.setShippingCost(shipCost);
            invoice.setGrandTotal(invoice.getGrandTotal().add(invoice.getShippingCost()));
        }
        repository.save(invoice);
        shippingAddressRepository.delete(shippingAddress);
        return MessageUtil.builder().message("Cập nhật địa chỉ giao hàng thành công !").status(1).type("bg-success").build();
    }

    @Override
    public MessageUtil huyGh(String codeBill) {
        Invoice invoice = repository.getReferenceById(codeBill);
        invoice.setShipping(false);
        invoice.setGrandTotal(invoice.getGrandTotal().subtract(invoice.getShippingCost()));
        invoice.setShippingCost(BigDecimal.ZERO);
        repository.save(invoice);
        return MessageUtil.builder().message("Hủy giao hàng thành công !").status(1).type("bg-success").build();
    }

    @Override
    public MessageUtil updateStatus(String codeBill, String note) {
        Invoice invoice = repository.getReferenceById(codeBill);
        if (invoice.getStatus().equals("1")) {
            invoice.setStatus("2");
        } else if (invoice.getStatus().equals("2")) {
            invoice.setStatus("3");
        } else if (invoice.getStatus().equals("3")) {
            invoice.setStatus("4");
        }else if (invoice.getStatus().equals("0")) {
            return MessageUtil.builder().message("Cập nhật thất bại !").status(1).type("bg-danger").object(codeBill).build();
        }
        invoice = repository.save(invoice);
        Transaction transaction = Transaction.builder()
                .invoice(invoice)
                .createDate(LocalDateTime.now())
                .note(note)
                .status(invoice.getStatus())
                .name(UserUltil.getUser().getName())
                .build();
        transactionRepository.save(transaction);
        return MessageUtil.builder().message("Cập nhật thành công !").status(1).type("bg-success").object(codeBill).build();
    }

    @Override
    public MessageUtil huyDh(String codeBill, String note) {
        Invoice invoice = repository.getReferenceById(codeBill);
        invoice.setStatus("-1");
        invoice = repository.save(invoice);
        Transaction transaction = Transaction.builder()
                .invoice(invoice)
                .createDate(LocalDateTime.now())
                .note(note)
                .status(invoice.getStatus())
                .name(UserUltil.getUser().getName())
                .build();
        transactionRepository.save(transaction);
        return MessageUtil.builder().message("Hủy thành công !").status(1).type("bg-success").object(codeBill).build();
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
