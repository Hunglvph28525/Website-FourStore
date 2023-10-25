package com.poly.datn.repository;

import com.poly.datn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("""
         from User where username = :u
    """)
    Optional<User> getByUser(@Param("u") String username);


    @Query(""" 
    from User where email = :u 
    """)
    Optional<User> getByEmail(@Param("u") String email);
}




