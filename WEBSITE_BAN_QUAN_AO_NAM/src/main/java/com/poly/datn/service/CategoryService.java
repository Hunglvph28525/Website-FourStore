package com.poly.datn.service;

import com.poly.datn.dto.CategoryDto;
import com.poly.datn.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<CategoryDto> getAll();

    List<Category> fillAll();

    void add(Category category);

    Category detail(Long id);

    void delete(Long id);

}
