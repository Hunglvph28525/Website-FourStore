package com.poly.datn.util;


import com.poly.datn.entity.User;
import com.poly.datn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class UserUltil {
    @Autowired
    private static UserService userService;

    public static User getUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.getUsername());

        return user;
//        return new User();
    }
}
