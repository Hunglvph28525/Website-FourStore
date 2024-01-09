package com.poly.datn.service.impl;

import com.poly.datn.dto.*;
import com.poly.datn.entity.*;
import com.poly.datn.entity.composite.CartId;
import com.poly.datn.repository.*;
import com.poly.datn.service.CartService;
import com.poly.datn.util.Fomater;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PromotionRepository promotionRepository;


    @Override
    public MessageUtil addtocart(Long sp, Long size, Long color, Integer quantity) {
        ProductDetail productDetail = productDetailRepository.getProduct(sp, color, size);
        Cart cart;
        if (cartRepository.existsByUser_Id(UserUltil.getUser().getId())) {
            cart = cartRepository.getByUser_Id(UserUltil.getUser().getId());
        } else {
            cart = cartRepository.save(Cart.builder()
                    .user(UserUltil.getUser())
                    .createDate(LocalDateTime.now())
                    .editDate(LocalDateTime.now())
                    .status("1")
                    .build());
        }
        List<CartDetail> details = cartDetailRepository.getCartDetail(cart.getId());
        Optional<CartDetail> checkDetail1 = details.stream().filter(x -> x.getCartId().getProductDetail().getId() == productDetail.getId()).findFirst();
        CartDetail cartDetail = new CartDetail();
        if (checkDetail1.isPresent()) {
            cartDetail = checkDetail1.get();
            int quantitycheck = cartDetail.getQuantity() + quantity;
            if (quantitycheck > productDetail.getQuantity()) {
                return MessageUtil.builder().message(" Số lượng vượt quá số lượng tồn !").status(1).type("bg-success").build();
            }
            cartDetail.setQuantity(quantitycheck);
        } else {
            cartDetail = CartDetail.builder()
                    .cartId(CartId.builder()
                            .cart(cart)
                            .productDetail(productDetail)
                            .build())
                    .quantity(quantity)
                    .price(productDetail.getPrice().doubleValue())
                    .build();
        }
        cartDetailRepository.save(cartDetail);
        return MessageUtil.builder().message("Thêm thành công !").status(1).type("bg-success").build();

    }

    @Override
    public GioHangDto getGioHang() {
        User user = UserUltil.getUser();
        if (user == null) return new GioHangDto();
        Cart cart = cartRepository.getByUser_Id(user.getId());
        if (cart == null) {
            cart = cartRepository.save(Cart.builder()
                    .user(UserUltil.getUser())
                    .createDate(LocalDateTime.now())
                    .editDate(LocalDateTime.now())
                    .status("1")
                    .build());
            cart = cartRepository.save(cart);
        }
        List<CartDetail> cartDetails = cartDetailRepository.getCartDetail(cart.getId());
        List<CartDetailDto> details = cartDetails.stream().map(x ->
                CartDetailDto.builder()
                        .product(new ProductDetailDto(x.getCartId().getProductDetail()))
                        .quantity(x.getQuantity())
                        .price(x.getPrice())
                        .priceFomat(Fomater.fomatTien().format(x.getPrice()))
                        .build()).collect(Collectors.toList());
        double total = cartDetails.stream().mapToDouble(x -> x.getPrice() * x.getQuantity()).sum();
        return GioHangDto.builder()
                .cartDetails(details)
                .tongSp(details.stream().count())
                .total(total)
                .totalFomat(Fomater.fomatTien().format(total))
                .build();
    }

    @Override
    public MessageUtil createOrder(List<SelectedProduct> selectedProducts) {
        User user = UserUltil.getUser();
        if (user == null)
            return MessageUtil.builder().message("Đã xảy ra lỗi vui lòng đăng nhập lại !").status(0).type("bg-danger").build();
        Address address = addressRepository.getAddressMacDinh(user.getId(), "on");
        List<Address> addresses = addressRepository.findByIdKhachHang(user.getId());
        List<CartDetailDto> cartDetailDtos = new ArrayList<>();
        selectedProducts.stream().forEach(x -> {
            ProductDetail productDetail = productDetailRepository.getReferenceById(x.getProductId());
            cartDetailDtos.add(CartDetailDto.builder()
                    .quantity(x.getQuantity())
                    .price(productDetail.getPrice().doubleValue())
                    .priceFomat(Fomater.fomatTien().format(productDetail.getPrice().doubleValue()))
                    .product(new ProductDetailDto(productDetail))
                    .build());
        });
        int total = cartDetailDtos.stream().mapToInt(x -> (int) (x.getPrice() * x.getQuantity())).sum();
        int shippingCost = 25000;
        if (address != null) {
            if (address.getProvince().equals("Thành phố Hà Nội")) shippingCost = 15000;
        } else {
            shippingCost = 0;
        }
        OrderDetailDto detailDto = OrderDetailDto.builder()
                .address(address)
                .addresses(addresses)
                .products(cartDetailDtos)
                .promotion(null)
                .total(total)
                .totalFomat(Fomater.fomatTien().format(total))
                .giaGiam(0)
                .giaGiamFomat(Fomater.fomatTien().format(0))
                .shipping(true)
                .shippingCost(shippingCost)
                .shippingCostFomat(Fomater.fomatTien().format(shippingCost))
                .grandTotal(total + shippingCost)
                .grandTotalFomat(Fomater.fomatTien().format(total + shippingCost))
                .paymentMethod(null)
                .name(user.getName())
                .sdt(user.getPhoneNumber())
                .build();

        return MessageUtil.builder().message("Thêm thành công !").status(1).type("bg-success").object(detailDto).build();
    }

    @Override
    public MessageUtil deleteProduct(Long idSp) {
        User user = UserUltil.getUser();
        if (user == null)
            return MessageUtil.builder().message("Đã xảy ra lỗi vui lòng đăng nhập lại !").status(1).type("bg-danger").build();
        Cart cart = cartRepository.getByUser_Id(user.getId());
        ProductDetail productDetail = productDetailRepository.getReferenceById(idSp);
        CartId cartId = new CartId(productDetail, cart);
        cartDetailRepository.deleteById(cartId);
        return MessageUtil.builder().message("Xóa thành công !").status(1).type("bg-success").build();
    }

    @Override
    public MessageUtil newAddressOrder(OrderDetailDto detailDto, String name, String sdt, String province, String district, String ward, String street) {
        if (UserUltil.getUser() == null)
            return MessageUtil.builder().message("Đã xảy ra lỗi vui lòng đăng nhập lại !").status(1).type("bg-danger").build();
        Address address = Address.builder()
                .user(UserUltil.getUser())
                .province(province)
                .district(district)
                .ward(ward)
                .street(street)
                .build();
        List<Address> addresses = addressRepository.findByIdKhachHang(UserUltil.getUser().getId());
        if (addresses.stream().count() < 1) {
            address.setStatus("on");
        } else {
            address.setStatus("off");
        }
        address = addressRepository.save(address);
        addresses = addressRepository.findByIdKhachHang(UserUltil.getUser().getId());
        detailDto.setAddress(address);
        int shippingCost = 25000;
        if (detailDto.getAddress() != null) {
            if (detailDto.getAddress().getProvince().equals("Thành phố Hà Nội")) shippingCost = 15000;
        } else {
            shippingCost = 0;
        }
        detailDto.setAddresses(addresses);
        detailDto.setName(name);
        detailDto.setSdt(sdt);
        detailDto.setShippingCost(shippingCost);
        detailDto.setShippingCostFomat(Fomater.fomatTien().format(shippingCost));
        detailDto.setGrandTotal(detailDto.getTotal() + detailDto.getShippingCost() - detailDto.getGiaGiam());
        detailDto.setGrandTotalFomat(Fomater.fomatTien().format(detailDto.getGrandTotal()));
        return MessageUtil.builder().message("thêm địa chỉ thành công !").status(1).type("bg-success").object(detailDto).build();
    }

    @Override
    public MessageUtil editAddressOrder(OrderDetailDto detailDto, Long idAddress) {
        User user = UserUltil.getUser();
        if (user == null)
            return MessageUtil.builder().message("Đã xảy ra lỗi vui lòng đăng nhập lại !").status(1).type("bg-danger").object(detailDto).build();
        Address address = addressRepository.getReferenceById(idAddress);
        detailDto.setAddress(address);
        int shippingCost = 25000;
        if (detailDto.getAddress() != null) {
            if (detailDto.getAddress().getProvince().equals("Thành phố Hà Nội")) shippingCost = 15000;
        } else {
            shippingCost = 0;
        }
        detailDto.setShippingCost(shippingCost);
        detailDto.setShippingCostFomat(Fomater.fomatTien().format(shippingCost));
        detailDto.setGrandTotal(detailDto.getTotal() + detailDto.getShippingCost() - detailDto.getGiaGiam());
        detailDto.setGrandTotalFomat(Fomater.fomatTien().format(detailDto.getGrandTotal()));
        return MessageUtil.builder().message("thêm địa chỉ thành công !").status(1).type("bg-success").object(detailDto).build();
    }

    @Override
    public MessageUtil addPgg(OrderDetailDto detailDto, Long idPgg) {
        User user = UserUltil.getUser();
        if (user == null)
            return MessageUtil.builder().message("Đã xảy ra lỗi vui lòng đăng nhập lại !").status(1).type("bg-danger").object(detailDto).build();
        Promotion promotion = detailDto.getPromotion();
        if (promotion != null) {
            promotion.setQuantity(promotion.getQuantity() + 1);
            promotionRepository.save(promotion);
        }
        promotion = promotionRepository.getReferenceById(idPgg);
        if (detailDto.getTotal().doubleValue() >= promotion.getMinValue().doubleValue() && promotion.getQuantity() > 0) {
            int giaGiam = detailDto.getTotal() * promotion.getDiscountValue() / 100;
            if (giaGiam > promotion.getMaxValue().doubleValue()){
                giaGiam = promotion.getMaxValue().intValue();
            }
            detailDto.setGiaGiam(giaGiam);
            detailDto.setGiaGiamFomat(Fomater.fomatTien().format(detailDto.getGiaGiam()));
            detailDto.setGrandTotal(detailDto.getTotal() + detailDto.getShippingCost() - detailDto.getGiaGiam());
            detailDto.setGrandTotalFomat(Fomater.fomatTien().format(detailDto.getGrandTotal()));
            detailDto.setPromotion(promotion);
            return MessageUtil.builder().message("Áp dụng phiếu giảm giá thành công !").status(1).type("bg-success").object(detailDto).build();
        } else {
            return MessageUtil.builder().message("Thất bại vì đơn hàng không đủ điều kiện !").status(0).type("bg-danger").object(detailDto).build();
        }
    }

    @Override
    public MessageUtil deleteCart(List<SelectedProduct> selectedProducts) {
        User user = UserUltil.getUser();
        if (user == null ) return MessageUtil.builder().message("Đã xảy ra lỗi vui lòng đăng nhập lại !").status(1).type("bg-danger").build();
        Cart cart = cartRepository.getByUser_Id(user.getId());
        selectedProducts.stream().forEach( x -> {
            ProductDetail productDetail = productDetailRepository.getReferenceById(x.getProductId());
            CartId  cartId = new CartId(productDetail,cart);
            cartDetailRepository.deleteById(cartId);
        });
        return MessageUtil.builder().message("Xóa sản phẩm khỏi giỏ hàng thành công !").status(1).type("bg-success").build();
    }
}
