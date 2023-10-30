package com.poly.datn.service;

import com.poly.datn.entity.Category;

import java.util.List;

public interface CategoryService {


    List<Category> getAll();

    void save(Category category);

}
