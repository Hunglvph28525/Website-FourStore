package com.poly.datn.dto;

import java.text.DecimalFormat;

public interface ListSPBanChay {

    String getProductName();

    String getUrl();

    Integer getQuantity();

    Double getPrice();

    String getPricec();  //doanh thu

    DecimalFormat df = new DecimalFormat("#,###");


}
