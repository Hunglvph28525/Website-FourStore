package com.poly.datn.repository;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Long> {
    Promotion getById(Long id);

    @Query("select new com.poly.datn.dto.PromotionDto(c) from Promotion c")
    List<PromotionDto> findAllDto();
}
