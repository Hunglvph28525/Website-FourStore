package com.poly.datn.config;

import com.poly.datn.service.InvoiceService;
import com.poly.datn.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class UpdateJopStatus {

    @Autowired
    private PromotionService service;
    @Autowired
    private InvoiceService invoiceService;

    @Scheduled(fixedRate = 60000)
    public void schedulePromotionProgramUpdate() {
        System.out.println("đã update");
        service.jobUpdate();
    }

    @Scheduled(cron = "0 0 23 * * *")
    public void scheduleInvoiceProgramUpdate() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        if (currentTime.getHour() == 23 && currentTime.getMinute() == 0 && currentTime.getSecond() == 0) {
            System.out.println("đã xóa khi hết ngày");
            invoiceService.deleteInvoice();
        }
    }
}
