package com.poly.datn.service.impl;

import com.poly.datn.entity.Brand;
import com.poly.datn.entity.Category;
import com.poly.datn.repository.CategoryRepository;
import com.poly.datn.service.CategoryService;
import com.poly.datn.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;


    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }


    @Override
    public MessageUtil save(Category dto) {
        if (categoryRepository.existsByName(dto.getName())){
            return MessageUtil.builder().status(0).message("Thêm thất bại vì tên danh mục đã có !").type("bg-danger").build();
        }else {
            categoryRepository.save(dto);
            return MessageUtil.builder().status(1).message("Thêm thành công !").type("bg-success").build();
        }
    }

}
