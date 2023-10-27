package com.poly.datn.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WebController {
    @GetMapping()
    public String homeLocal(){
        return "web/index";
    }
    @GetMapping("/home")
    public String home(){
        return "web/index";
    }
}
