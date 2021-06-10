package com.linesh.mc.api;

import com.linesh.mc.model.OrderData;
import com.linesh.mc.service.PackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PackageController {

    @Autowired
    PackageService packageService;

    @PostMapping("sort-package")
    public void createOrder(@RequestBody OrderData orderData) {
        log.info("Processing Order Id {}", orderData.getOrderId());
        packageService.sortAndSendPackages(orderData);
        log.info("Successfully Processed Order Id {}", orderData.getOrderId());
    }
}