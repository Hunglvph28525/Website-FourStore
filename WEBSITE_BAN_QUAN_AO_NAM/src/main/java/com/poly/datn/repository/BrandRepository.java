package com.poly.datn.repository;

import com.poly.datn.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Boolean existsByName(String name);

    @Query("select c from Brand c order by c.id desc ")
    List<Brand> getAll();

}
