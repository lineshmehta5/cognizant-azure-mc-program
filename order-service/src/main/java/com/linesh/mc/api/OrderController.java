package com.linesh.mc.api;

import com.linesh.mc.model.OrderData;
import com.linesh.mc.model.OrderServiceResponse;
import com.linesh.mc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<OrderServiceResponse> createOrder(@RequestBody OrderData orderData) {
        return orderService.processOrder(orderData);
    }
}