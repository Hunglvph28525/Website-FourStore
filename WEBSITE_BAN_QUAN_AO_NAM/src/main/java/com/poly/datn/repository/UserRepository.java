package com.poly.datn.repository;

import com.poly.datn.dto.Customernew;
import com.poly.datn.dto.ProductDto;
import com.poly.datn.dto.UserDto;
import com.poly.datn.entity.Address;
import com.poly.datn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
                 from User where username = :u
            """)
    Optional<User> getByUser(@Param("u") String username);


    @Query(""" 
            from User where email = :u 
            """)
    Optional<User> getByEmail(@Param("u") String email);




    @Query("select new com.poly.datn.dto.UserDto(c) from User c where c.status = 'onKH' or c.status='offKH' order by c.Id DESC ")
    List<UserDto> getAll();


    @Query("select new com.poly.datn.dto.UserDto(c) from User c where c.status = :status   order by c.Id desc ")
    List<UserDto> tkStatus(String status);


    @Query("select new com.poly.datn.dto.UserDto(c) from User c where c.status = 'onNV' or c.status='offNV' order by c.Id DESC ")
    List<UserDto> getAllStaff();


    // số lượng khách hàng
    @Query(value = "select count(id) from Users where status = 'onKH'\n", nativeQuery = true)
    Integer getKhachHang();

    //top khách hàng mới
    @Query(value = "select top 5 id 'id',name 'name',email 'email',phone_number 'phoneNumber',gender 'gioiTinh'  from users where status = 'onKH' \n" +
            "ORDER BY id desc", nativeQuery = true)
    List<Customernew> getNewKH();


    @Query("select c from Address c where c.user.id=:id")
    List<Address> getAddressByUser(Long id);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByUsername(String username);

    boolean existsBycccd(String cccd);









    @Query(""" 
            from User where email = :u and status = 'onNV'
            """)
    Optional<User> getByEmailNV(@Param("u") String email);

    @Query("select new com.poly.datn.dto.UserDto(c) from User c where c.status = 'onNV' or c.status='offNV' order by c.Id DESC ")
    List<UserDto> getAllNV();


    @Query("select new com.poly.datn.dto.UserDto(c) from User c where c.status = :status   order by c.Id desc ")
    List<UserDto> tkStatusNV(String status);

}




