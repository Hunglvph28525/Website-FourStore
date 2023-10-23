package com.poly.datn.service;

import com.poly.datn.dto.CategoryDto;
import com.poly.datn.entity.Category;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAll();

    List<Category> fillAll();

    void Save(CategoryDto dto);

    CategoryDto detail(Long id);

    void delete(Long id);

}
