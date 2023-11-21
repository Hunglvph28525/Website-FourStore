package com.poly.datn.controller.admin;

import com.poly.datn.dto.AddToCartRequest;
import com.poly.datn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin("*")
public class ApiController {
    @Autowired
    private ProductService productService;

    @GetMapping("/admin/api/product/{id}")
    public ResponseEntity<?> detailProductApi(@PathVariable("id") Long id ){
        return  ResponseEntity.ok(productService.getDetailtoCart(id));
    }
    @PostMapping("admin/api/sale/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest addToCart){
        System.out.println(addToCart);
        return  ResponseEntity.ok("ok");
    }
}
