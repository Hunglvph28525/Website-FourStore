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
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<InvoiceDto> hoaDonMoi() {
        List<Invoice> list = repository.getDonHangMoi();
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
        list.stream().forEach(x -> {
            ProductDetail productDetail = productDetailRepository.getReferenceById(x.getInvoiceId().getProductDetail().getId());
            productDetail.setQuantity(productDetail.getQuantity() + x.getQuantity());
            productDetailRepository.save(productDetail);
        });
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
            detailRepository.save(detail);
        }

        productDetail.setQuantity(productDetail.getQuantity() - 1);
        productDetailRepository.save(productDetail);
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
            BigDecimal giaGiam = invoice.getTotal().multiply(BigDecimal.valueOf(promotion.getDiscountValue()).divide(new BigDecimal("100")));
            if (giaGiam.doubleValue() > promotion.getMaxValue().doubleValue()){
                giaGiam = promotion.getMaxValue();
            }
            invoice.setGiaGiam(giaGiam);
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
        } else if (invoice.getStatus().equals("0")) {
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
        List<InvoiceDetail> invoiceDetails = detailRepository.getByCodeBill(codeBill);
        invoiceDetails.stream().forEach(x -> {
            ProductDetail productDetail = productDetailRepository.getReferenceById(x.getInvoiceId().getProductDetail().getId());
            productDetail.setQuantity(productDetail.getQuantity() + x.getQuantity());
            productDetailRepository.save(productDetail);
        });
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

    @Override
    public MessageUtil ttDonHang(String codeBill, String note) {
        Invoice invoice = repository.getReferenceById(codeBill);
        invoice.setPaymentInfo(note);
        invoice.setPaymentStatus("1");
        invoice.setPaymentDate(LocalDateTime.now());
        invoice = repository.save(invoice);
        updateStatus(codeBill,note);
        return MessageUtil.builder().message("Xác nhận thanh toán thành công !").status(1).type("bg-success").object(invoice).build();
    }

    @Override
    public void deleteInvoice() {
        List<Invoice> invoices = repository.getStatus("0");
        repository.deleteAll(invoices);
    }

    @Override
    public Object getInvoceByUser() {
        if (UserUltil.getUser() == null) return null;
        List<Invoice> invoices = repository.getInvoiceByUser(UserUltil.getUser().getId());
        List<InvoiceDto> invoiceDtos = invoices.stream().map(x -> new InvoiceDto(x)).collect(Collectors.toList());
        return invoiceDtos;
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

    @Override
    public void xuatHd(String codeBill, HttpServletResponse response) throws IOException {
        Invoice invoice = repository.getReferenceById(codeBill);
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph xWPFParagraph = document.createParagraph();
        XWPFRun run = xWPFParagraph.createRun();
        XWPFParagraph titleGraph = document.createParagraph();
        titleGraph.setAlignment(ParagraphAlignment.CENTER);
        String title = "Cửa hàng quần áo FourStore";
        XWPFRun titleRun = titleGraph.createRun();
        titleRun.setBold(true);
        titleRun.setText(title);
        XWPFParagraph xWPFParagraph1 = document.createParagraph();
        xWPFParagraph1.setAlignment(ParagraphAlignment.CENTER);
        run = xWPFParagraph1.createRun();
        run.setText("ĐC: Phố Trinh Văn Bô , Nam Từ Liêm , Hà Nội");
        XWPFParagraph xWPFParagraph2 = document.createParagraph();
        xWPFParagraph2.setAlignment(ParagraphAlignment.CENTER);
        run = xWPFParagraph2.createRun();
        run.setText("SĐT:0123456789");
        XWPFParagraph xWPFParagraph3 = document.createParagraph();
        xWPFParagraph3.setAlignment(ParagraphAlignment.CENTER);
        run = xWPFParagraph3.createRun();
        run.setText("HOÁ ĐƠN BÁN QUẦN ÁO");
        XWPFParagraph xWPFParagraph4 = document.createParagraph();
        run = xWPFParagraph4.createRun();
        run.setText("Khách Hàng :" + (invoice.getUser() == null ? "Khách hàng lẻ" : invoice.getUser().getName()));
        XWPFParagraph xWPFParagraph0 = document.createParagraph();
        run = xWPFParagraph0.createRun();
        run.setText("Mã Hoá Đơn :" + invoice.getCodeBill());
        XWPFParagraph xWPFParagraph5 = document.createParagraph();
        run = xWPFParagraph5.createRun();
        run.setText("Địa Chỉ :" + address(invoice.getShippingAddress()));
        XWPFParagraph xWPFParagraph6 = document.createParagraph();
        run = xWPFParagraph6.createRun();
        run.setText("Số Điện Thoại :" + (invoice.getSdtNhan() == null ? (invoice.getUser() == null ? "Không có" : invoice.getUser().getPhoneNumber()) : invoice.getSdtNhan()));
        XWPFParagraph xWPFParagraph7 = document.createParagraph();
        run = xWPFParagraph7.createRun();
        run.setText("Ngày lập :" + invoice.getCreateDate());
        XWPFParagraph xWPFParagraph13 = document.createParagraph();
        run = xWPFParagraph13.createRun();
        run.setText("  ");
        XWPFTable table = document.createTable();
        table.setWidth(10000);
        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.getCell(0).setText("tên Sản Phẩm");
        tableRowOne.addNewTableCell().setText("Số Lượng");
        tableRowOne.addNewTableCell().setText("Đơn Giá)");
        tableRowOne.addNewTableCell().setText("Thành Tiền");
        int row = 0;
        List<InvoiceDetailDto> invoiceDetailDtos = detailRepository.getAllInvoi(invoice.getCodeBill());
        for (InvoiceDetailDto x : invoiceDetailDtos) {
            XWPFTableRow tableRowTwo = table.createRow();
            tableRowTwo.getCell(0).setText(x.getName());
            tableRowTwo.getCell(1).setText(x.getQuantity().toString());
            tableRowTwo.getCell(2).setText(x.getPriceFomat());
            tableRowTwo.getCell(3).setText(Fomater.fomatTien().format(x.getPrice() * x.getQuantity()));
            row++;
        }
        XWPFParagraph xWPFParagraph14 = document.createParagraph();
        run = xWPFParagraph14.createRun();
        run.setText("Tổng tiền hàng : " + Fomater.fomatTien().format(invoice.getTotal()));
        XWPFParagraph xWPFParagraph8 = document.createParagraph();
        run = xWPFParagraph8.createRun();
        run.setText("Giảm Giá :" + Fomater.fomatTien().format(invoice.getGiaGiam()));
        XWPFParagraph xWPFParagraph9 = document.createParagraph();
        run = xWPFParagraph9.createRun();
        run.setText("Tổng Tiền Thanh Toán :" + Fomater.fomatTien().format(invoice.getGrandTotal()));
        XWPFParagraph xWPFParagraph10 = document.createParagraph();
        run = xWPFParagraph10.createRun();
        run.setText("tiền Khách trả :" + Fomater.fomatTien().format(invoice.getUserPay()));
        XWPFParagraph xWPFParagraph15 = document.createParagraph();
        run = xWPFParagraph15.createRun();
        run.setText("tiền trả lại :" + Fomater.fomatTien().format(invoice.getTienThoi()));
        XWPFParagraph xWPFParagraph11 = document.createParagraph();
        xWPFParagraph11.setAlignment(ParagraphAlignment.RIGHT);
        run = xWPFParagraph11.createRun();
        run.setText("Người Lập Hoá Đơn");
        XWPFParagraph xWPFParagraph12 = document.createParagraph();
        xWPFParagraph12.setAlignment(ParagraphAlignment.RIGHT);
        run = xWPFParagraph12.createRun();
        run.setText(UserUltil.getUser().getName());
        OutputStream outputStream = response.getOutputStream();
        document.write(outputStream);
        outputStream.close();
        document.close();
    }

    private String address(ShippingAddress x) {
        if (x == null) return "Không";
        return x.getStreet() + ", " + x.getWard() + ", " + x.getDistrict() + ", " + x.getProvince();
    }

}
