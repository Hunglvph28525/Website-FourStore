package com.poly.datn.service.impl;

import com.poly.datn.entity.TypeProduct;
import com.poly.datn.repository.TypeProductRepository;
import com.poly.datn.service.TypeProductService;
import com.poly.datn.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeProductServiceImpl implements TypeProductService {
    @Autowired
    private TypeProductRepository repository;

    @Override
    public MessageUtil save(TypeProduct typeProduct) {
        if (repository.existsByName(typeProduct.getName())){
            return MessageUtil.builder().status(0).message("Thêm thất bại vì Loại sản phẩm đã tồn tại !").type("bg-danger").build();
        }else {
            repository.save(typeProduct);
            return MessageUtil.builder().status(1).message("Thêm thành công !").type("bg-success").build();
        }

    }

    @Override
    public Object detail(Long id) {
        return repository.getReferenceById(id);
    }

    @Override
    public List<TypeProduct> getAll() {
        return repository.findAll();
    }


}
