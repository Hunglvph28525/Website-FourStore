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
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(user.getUsername());
//
//        return user;
        return  User.builder().name("Lê Việt Hưng").avatar("https://res.cloudinary.com/dg8hhxkah/image/upload/v1698080470/avartar/vedidtoxs2wn2khvtekq.jpg").build();
    }
}
