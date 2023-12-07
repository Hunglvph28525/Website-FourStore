package com.poly.datn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;


public interface BieuDoCot {

    LocalDateTime getCreateDate();


    Double getPrice();


}
