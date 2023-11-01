package com.poly.datn.config;

import com.poly.datn.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateJopStatus {

    @Autowired
    private PromotionService service;

    @Scheduled(fixedRate = 600000) //10p
    public void schedulePromotionProgramUpdate() {
        System.out.println("đã update");
        service.jopUpdateStatus();
    }
}
