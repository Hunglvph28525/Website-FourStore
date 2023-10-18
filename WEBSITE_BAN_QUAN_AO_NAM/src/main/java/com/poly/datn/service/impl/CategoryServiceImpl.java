package com.poly.datn.service.impl;

import com.poly.datn.dto.CategoryDto;
import com.poly.datn.entity.Category;
import com.poly.datn.repository.CategoryRepository;
import com.poly.datn.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void add(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Category detail(Long id) {
        return categoryRepository.getCoAoById(id);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);

    }

    @Override
    public List<CategoryDto> getAll() {
        return categoryRepository.getAll();
    }

    @Override
    public List<Category> fillAll() {
        return categoryRepository.findAll();
    }
}
