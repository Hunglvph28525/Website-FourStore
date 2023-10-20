package com.poly.datn.repository;

import com.poly.datn.entity.Role;
import com.poly.datn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    @Query("""  
         from Role where roleName = :u 
    """)
    Role getByName(@Param("u") String rolename);
}
