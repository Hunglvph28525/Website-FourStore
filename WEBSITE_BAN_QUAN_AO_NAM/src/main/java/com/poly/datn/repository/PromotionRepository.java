package com.poly.datn.repository;

import com.poly.datn.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Long> {
    Promotion getById(Long id);
}
