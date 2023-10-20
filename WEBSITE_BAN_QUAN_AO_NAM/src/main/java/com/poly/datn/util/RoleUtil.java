package com.poly.datn.util;

public enum RoleUtil {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String value;

    RoleUtil(final String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
