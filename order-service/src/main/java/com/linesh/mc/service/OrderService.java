package com.linesh.mc.service;

import com.linesh.mc.model.OrderData;
import com.linesh.mc.model.OrderServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private PackageService packageService;

    public Mono<OrderServiceResponse> processOrder(OrderData orderData) {
        return packageService.sendToPackageService(orderData);
    }
}
