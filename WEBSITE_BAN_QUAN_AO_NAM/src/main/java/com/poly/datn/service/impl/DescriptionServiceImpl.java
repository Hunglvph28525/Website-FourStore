package com.poly.datn.service.impl;

import com.poly.datn.entity.Description;
import com.poly.datn.repository.DescriptionRepository;
import com.poly.datn.service.DescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DescriptionServiceImpl implements DescriptionService {

    @Autowired
    DescriptionRepository descriptionRepository;
    @Override
    public Page<Description> phanTrang(Integer pageNum, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNum, pageNo);
        return descriptionRepository.findAll(pageable);

    }

    @Override
    public void add(Description description) {
        descriptionRepository.save(description);
    }

    @Override
    public Description detail(Long id) {
        return descriptionRepository.getById(id);
    }
}
