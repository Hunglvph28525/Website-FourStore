package com.poly.datn.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloud = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dg8hhxkah",
                "api_key", "689858737754194",
                "api_secret", "_Bjo7wwguxDZt9DiZ882PLuox10",
                "secure",true));
        return cloud ;
    }

}
