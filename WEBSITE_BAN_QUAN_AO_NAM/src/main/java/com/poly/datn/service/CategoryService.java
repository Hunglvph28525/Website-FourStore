package com.poly.datn.service;

import com.poly.datn.entity.Category;
import com.poly.datn.util.MessageUtil;

import java.util.List;

public interface CategoryService {


    List<Category> getAll();

    MessageUtil save(Category category);

}
