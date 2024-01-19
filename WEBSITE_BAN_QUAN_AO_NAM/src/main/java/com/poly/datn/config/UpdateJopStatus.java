package com.poly.datn.config;

import com.poly.datn.entity.Invoice;
import com.poly.datn.service.InvoiceService;
import com.poly.datn.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UpdateJopStatus {

    @Autowired
    private PromotionService service;
    @Autowired
    private InvoiceService invoiceService;


    @Scheduled(fixedRate = 30000)
    public void schedulePromotionProgramUpdate() {
        System.out.println("đã update");
        service.jobUpdate();
    }

    @Scheduled(cron = "0 * * * * *") // Chạy mỗi giờ
    public void cleanupTask() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        System.out.println("Chạy cronjob ");
        List<Invoice> objectList = invoiceService.getStatus("0");
        for (Invoice obj : objectList) {
            LocalDateTime objectDateTime = obj.getCreateDate();
            long hoursDifference = java.time.Duration.between(objectDateTime, currentDateTime).toHours();
            if (hoursDifference >= 24) {
                invoiceService.deleteHdCho(obj);
                System.out.println("Xóa hóa đơn chờ ");
            }
        }
    }
}
