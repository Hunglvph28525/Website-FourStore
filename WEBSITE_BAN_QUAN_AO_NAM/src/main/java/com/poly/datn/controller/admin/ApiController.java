package com.poly.datn.controller.admin;


import com.poly.datn.dto.UpdateQuantityReq;
import com.poly.datn.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @Autowired
    private InvoiceService service;

    @PostMapping("/admin/sale/update-quantity")
    public ResponseEntity<?> updateSL(@RequestBody UpdateQuantityReq req){
        System.out.println(req.toString());
        return  ResponseEntity.ok( service.updateSL(req));
    }
}
