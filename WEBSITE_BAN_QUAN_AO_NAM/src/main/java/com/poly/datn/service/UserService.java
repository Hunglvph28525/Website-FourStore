package com.poly.datn.service;

import com.poly.datn.dto.UserDto;
import com.poly.datn.dto.UserRequest;
import com.poly.datn.entity.Address;
import com.poly.datn.entity.User;
import com.poly.datn.request.UserSignUpRequest;
import com.poly.datn.util.MessageUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

public interface UserService extends Converter<UserSignUpRequest, User>, UserDetailsService {

    List<User> getAll();

    void add(User user);

    Optional<User> detail(Long id);

    Optional<User> getById(Long id);

    Optional<User> getByUserName(String userName);

    User addUserToBill(User user);

    boolean isEmailExists(String email);

    boolean isCCCD(String cccd);




    List<UserDto> getAll(String status);

    User detailCustomer(Long id);

    MessageUtil add(UserRequest userRequest, MultipartFile file);


    MessageUtil updateSaff(User user);

    MessageUtil updateCustomer(User user);

    List<Address> findByIdDiaChi(Long cid);


    List<UserDto> getAllStaff();


    MessageUtil addStaff(UserRequest userRequest, MultipartFile file);

    User detailStaff(Long id);

    Address detailAddress(Long id);

    void updateAddress(Long idKH, Long idAddress);

    boolean isEmailExistsNV(String email);


    List<UserDto> getAllNV(String status);

    MessageUtil doiThongTin(String pass);

    String dangKy(Model model, UserSignUpRequest userSignUpRequest, RedirectAttributes attributes);

    String fogotPass(String email, RedirectAttributes attributes);
}


