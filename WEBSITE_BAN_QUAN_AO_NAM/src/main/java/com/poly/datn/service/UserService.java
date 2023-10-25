package com.poly.datn.service;

import com.poly.datn.entity.User;
import com.poly.datn.request.UserSignUpRequest;
import com.poly.datn.request.forgot_passwort.UserForgotPasswordRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends Converter<UserSignUpRequest,User>
        , UserDetailsService{
    List<User> getAll();

    void add(User user);

    Optional<User> detail(Long id);

    Optional<User> getById(Long id);

    Optional<User> getByUserName(String userName);


    boolean isEmailExists(String email);


    Optional<User> changePassword(UserForgotPasswordRequest userForgotPasswordRequest);
}

