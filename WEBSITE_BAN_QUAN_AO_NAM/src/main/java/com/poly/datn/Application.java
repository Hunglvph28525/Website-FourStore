package com.poly.datn;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
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

//
//    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
//            "cloud_name", "dg8hhxkah",
//            "api_key", "689858737754194",
//            "api_secret", "_Bjo7wwguxDZt9DiZ882PLuox10"));
}
