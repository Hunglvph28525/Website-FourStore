package com.poly.datn.dto;

import com.poly.datn.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    private Long Id;
    private String name;
    private Boolean gender;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private String avatar;
    private String status;
    private User user;
    private  String cccd;

    public UserDto(User user){
        this.Id = user.getId();
        this.name = user.getName();
        this.gender = user.getGender();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.avatar = user.getAvatar();
        this.status = user.getStatus();
        this.user = user;
        this.cccd = user.getCccd();
    }

}
