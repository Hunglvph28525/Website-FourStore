package com.poly.datn.request.forgot_passwort;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Pattern(regexp = "[a-z0-9]+",message = "chi duoc phep tu a den z vaf tu 0 den 9")
    private String userName;

    @NotEmpty(message = "mat khau khong duoc de trong")
    private String password;
    @NotEmpty(message = "mat khau khong duoc de trong")
    private String confirmPassword;
}
