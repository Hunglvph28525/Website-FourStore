package com.poly.datn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDateTime;


public interface BieuDoCot {

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDateTime getCreateDate();


    Double getPrice();


}
