package com.poly.datn.request.forgot_passwort;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserForgotPasswordRequest {

    @Pattern(regexp = "[a-zA-Z0-9]+",message = "chi duoc phep tu a den z vaf tu 0 den 9")
    private String userName;

    @NotEmpty(message = "mat khau khong duoc de trong")
    private String password;
    @NotEmpty(message = "mat khau khong duoc de trong")
    private String confirmPassword;

    public UserForgotPasswordRequest() {
        this.userName = "";
        this.password = "";
        this.confirmPassword = "";
    }
}
