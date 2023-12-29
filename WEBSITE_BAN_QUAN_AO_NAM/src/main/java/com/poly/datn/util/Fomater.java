package com.poly.datn.util;

import java.text.NumberFormat;
import java.util.Locale;

public class Fomater {
    static public NumberFormat fomatTien() {
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        currencyFormat.setMaximumFractionDigits(0);
        return currencyFormat;
    }
}
