package com.poly.datn.service;

import com.poly.datn.entity.Brand;
import com.poly.datn.util.MessageUtil;
import com.sun.mail.handlers.message_rfc822;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BrandService {
    List<Brand> getAll();

    MessageUtil save(String name, MultipartFile file) throws IOException;
}
