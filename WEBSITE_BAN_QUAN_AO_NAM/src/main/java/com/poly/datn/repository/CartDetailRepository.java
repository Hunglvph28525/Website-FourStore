package com.poly.datn.repository;

import com.poly.datn.entity.CartDetail;
import com.poly.datn.entity.composite.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, CartId> {
    @Query("select c from CartDetail c where c.cartId.cart.id =:id")
    List<CartDetail> getCartDetail(Long id);
}
