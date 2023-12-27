package com.poly.datn.repository;

import com.poly.datn.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Boolean existsByUser_Id(Long id);
    @Query("select c from Cart c where c.user.id =:id")
     Cart getByUser_Id(Long id);
}
