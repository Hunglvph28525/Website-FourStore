package com.poly.datn.repository;

import com.poly.datn.entity.Description;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescriptionRepository extends JpaRepository<Description,Long> {
    Description getById(Long id);
}
