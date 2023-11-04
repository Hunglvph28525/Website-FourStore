package com.poly.datn.repository;

import com.poly.datn.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color,Integer> {
    Color getById(Integer id);

    Boolean existsByName(String name);
}
