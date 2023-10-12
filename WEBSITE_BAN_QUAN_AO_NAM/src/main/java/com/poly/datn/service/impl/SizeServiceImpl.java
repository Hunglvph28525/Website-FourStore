package com.poly.datn.service.impl;

import com.poly.datn.entity.Size;
import com.poly.datn.repository.SizeRepository;
import com.poly.datn.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {
    @Autowired
    SizeRepository sizeRepository;

    @Override
    public List<Size> getAll() {
        return sizeRepository.findAll();
    }

    @Override
    public Page<Size> phanTrang(Integer pageNum, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNum, pageNo);
        return sizeRepository.findAll(pageable);
    }

    @Override
    public void add(Size size) {
        sizeRepository.save(size);

    }

    @Override
    public Size detail(Integer id) {
        return sizeRepository.getById(id);
    }

    @Override
    public void delete(Integer id) {
        sizeRepository.deleteById(id);
    }
}
