package com.linesh.mc.api;

import com.linesh.mc.model.OrderData;
import com.linesh.mc.model.OrderServiceResponse;
import com.linesh.mc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<OrderServiceResponse> createOrder(@RequestBody OrderData orderData) {
        ResponseEntity<OrderServiceResponse> response = orderService.processOrder(orderData);
        log.info("Request Processed!");
        return response;
    }
}