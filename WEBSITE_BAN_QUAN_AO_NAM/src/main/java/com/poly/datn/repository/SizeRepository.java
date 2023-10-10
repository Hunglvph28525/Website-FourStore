package com.poly.datn.repository;

import com.poly.datn.entity.Size;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface SizeRepository extends JpaRepository<Size,Integer> {
    Size getById(Integer id);
}
