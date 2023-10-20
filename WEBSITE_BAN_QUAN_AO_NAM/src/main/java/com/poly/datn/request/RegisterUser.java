package com.poly.datn.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegisterUser {
    @NotEmpty(message = "email khong duoc de trong")
    @Email(message = "email khong hop le")
    private String email;
    @Pattern(regexp = "[a-z0-9]+",message = "chi duoc phep tu a den z vaf tu 0 den 9")
    private String userName;

    @NotEmpty(message = "mat khau khong duoc de trong")
    private String password;
    @NotEmpty(message = "mat khau khong duoc de trong")
    private String confirmPassword;

}
