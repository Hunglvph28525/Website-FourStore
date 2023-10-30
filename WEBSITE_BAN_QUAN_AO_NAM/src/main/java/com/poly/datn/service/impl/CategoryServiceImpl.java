package com.poly.datn.service.impl;

import com.poly.datn.entity.Category;
import com.poly.datn.repository.CategoryRepository;
import com.poly.datn.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;


    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }


    @Override
    public void save(Category dto) {
            categoryRepository.save(dto);
    }

}
