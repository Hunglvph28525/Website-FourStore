package com.poly.datn.repository;

import com.poly.datn.dto.TtpeProductDto;
import com.poly.datn.entity.TypeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeProductRepository extends JpaRepository<TypeProduct, Long> {

    @Query("select new com.poly.datn.dto.TtpeProductDto(c) from TypeProduct c")
    List<TtpeProductDto> getAll();
}
