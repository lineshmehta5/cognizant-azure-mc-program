package com.linesh.mc.api;

import com.linesh.mc.model.OrderData;
import com.linesh.mc.model.OrderServiceResponse;
import com.linesh.mc.service.PackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class PackageController {

    @Autowired
    PackageService packageService;

    @PostMapping("create-order")
    public Mono<OrderServiceResponse> createOrder(@RequestBody OrderData orderData) {
        log.info("Inside PackageController.createOrder()");
        return Mono.just(new OrderServiceResponse());
    }
}