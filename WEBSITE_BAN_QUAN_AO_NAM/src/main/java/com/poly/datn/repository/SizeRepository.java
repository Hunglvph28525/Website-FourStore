package com.poly.datn.repository;

import com.poly.datn.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size,Integer> {
    Size getById(Integer id);
    Boolean existsByName(String name);
}
