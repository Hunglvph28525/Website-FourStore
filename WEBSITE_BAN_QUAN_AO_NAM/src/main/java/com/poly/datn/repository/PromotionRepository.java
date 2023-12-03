package com.poly.datn.repository;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Promotion getById(Long id);

    @Query("select new com.poly.datn.dto.PromotionDto(c) from Promotion c order by c.id desc ")
    List<PromotionDto> findAllDto();

    @Query("select new com.poly.datn.dto.PromotionDto(c) from Promotion c where c.status = :status order by c.id desc ")
    List<PromotionDto> findAllDto(String status);

    @Query("select c from Promotion c where c.status in ('1','0')")
    List<Promotion> getAllJop();

    Boolean existsByGiftCodeOrDiscountName(String giftCode, String name);

    Promotion getByGiftCode(String pgg);
}
