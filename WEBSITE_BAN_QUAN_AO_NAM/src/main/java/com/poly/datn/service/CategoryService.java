package com.poly.datn.service;

import com.poly.datn.entity.Category;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CategoryService {

    Page<Category> phanTrang(Integer pageNum, Integer pageNo);

    void add(Category category);

    Category detail(Long id);

    void delete(Long id);
}
