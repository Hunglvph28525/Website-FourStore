package com.poly.datn.repository;

import com.poly.datn.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color,Integer> {
    Color getById(Integer id);

    Boolean existsByName(String name);

    @Query("select c from Color c order by c.id desc ")
    List<Color> getAll();
}
