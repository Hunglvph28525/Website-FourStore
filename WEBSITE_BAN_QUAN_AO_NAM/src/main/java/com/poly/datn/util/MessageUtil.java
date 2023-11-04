package com.poly.datn.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MessageUtil {
    private Integer status;
    private String message;
    private String type;
    private Object object;

    public MessageUtil(Integer status, String message, String type) {
        this.status = status;
        this.message = message;
        this.type = type;
    }
}
