package com.poly.datn.repository;

import com.poly.datn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("""  
         from User where username = :u 
    """)
    User getByUser(@Param("u") String username);
//    @Query("select c from User c where c.username =:s")
//    User getUserByUsername(String user);
}
