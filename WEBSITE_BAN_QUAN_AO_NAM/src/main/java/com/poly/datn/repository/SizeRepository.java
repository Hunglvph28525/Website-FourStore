package com.poly.datn.repository;

import com.poly.datn.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size,Integer> {
    Size getById(Integer id);
    Boolean existsByName(String name);
    boolean existsById(Integer id);

    @Query("select c from Size c order by c.id desc")
    List<Size> getAll();
}
