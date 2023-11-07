package com.poly.datn.service.impl;

import com.poly.datn.entity.Brand;
import com.poly.datn.repository.BrandRepository;
import com.poly.datn.service.BrandService;
import com.poly.datn.service.ImageService;
import com.poly.datn.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
 public class BrandServiceImpl implements BrandService {
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    private ImageService imageService;

    @Override
    public Brand detail(Long id) {
        return brandRepository.getReferenceById(id);
    }

    @Override
    public List<Brand> getAll() {
        return brandRepository.getAll();
    }

    @Override
    public MessageUtil save(Brand brand) {
        if (brandRepository.existsByName(brand.getName())){
            return MessageUtil.builder().status(0).message("Thêm thất bại vì tên thương hiệu đã có !").type("bg-danger").build();
        }else {
            brandRepository.save(brand);
            return MessageUtil.builder().status(1).message("Thêm thành công !").type("bg-success").build();
        }

    }

}
