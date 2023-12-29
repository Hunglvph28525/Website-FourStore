package com.poly.datn.repository;

import com.poly.datn.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("select p from Address p where p.user.id=?1")
    List<Address> findByIdKhachHang(Long cid);

    @Query("select c from Address c where c.user.id =:idUser and c.status =:status")
    Address getAddressMacDinh(Long idUser, String status);

}
