package com.poly.datn.service.impl;

import com.poly.datn.dto.*;
import com.poly.datn.entity.*;
import com.poly.datn.entity.composite.CartId;
import com.poly.datn.repository.AddressRepository;
import com.poly.datn.repository.CartDetailRepository;
import com.poly.datn.repository.CartRepository;
import com.poly.datn.repository.ProductDetailRepository;
import com.poly.datn.service.CartService;
import com.poly.datn.util.Fomater;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
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
        List<CartDetail> cartDetails = cartDetailRepository.getCartDetail(cart.getId());
        List<CartDetailDto> details = cartDetails.stream().map(x ->
                CartDetailDto.builder()
                        .product(new ProductDetailDto(x.getCartId().getProductDetail()))
                        .quantity(x.getQuantity())
                        .price(x.getPrice())
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
        addresses.stream().forEach(System.out::println);
        List<CartDetailDto> cartDetailDtos = new ArrayList<>();
        selectedProducts.stream().forEach(x -> {
            ProductDetail productDetail = productDetailRepository.getReferenceById(x.getProductId());
            cartDetailDtos.add(CartDetailDto.builder()
                    .quantity(x.getQuantity())
                    .price(productDetail.getPrice().doubleValue())
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
                .promotion(new Promotion())
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


}
