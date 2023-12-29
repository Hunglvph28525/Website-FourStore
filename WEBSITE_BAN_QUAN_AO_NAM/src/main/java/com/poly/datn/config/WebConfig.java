package com.poly.datn.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.text.NumberFormat;
import java.util.Locale;

@Configuration
public class WebConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error"));
        registry.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error"));
        registry.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error"));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
