package com.linesh.mc.service;

import com.linesh.mc.enums.ProcessingStatus;
import com.linesh.mc.jms.PackageServiceDelegate;
import com.linesh.mc.model.OrderData;
import com.linesh.mc.model.OrderServiceResponse;
import com.linesh.mc.model.PackageServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private PackageServiceDelegate packageServiceDelegate;

    public ResponseEntity<OrderServiceResponse> processOrder(OrderData orderData) {
        orderData.setOrderId(UUID.randomUUID().toString());
        ResponseEntity<PackageServiceResponse> packageServiceResponseResponseEntity = packageServiceDelegate.sortPackage(orderData);
        OrderServiceResponse orderServiceResponse = new OrderServiceResponse();
        orderServiceResponse.setOrderId(orderData.getOrderId());
        orderServiceResponse.setCustomerId(orderData.getCustomerId());
        if (packageServiceResponseResponseEntity.getStatusCode().is2xxSuccessful()) {
            orderServiceResponse.setProcessingStatus(ProcessingStatus.PROCESSED);
            return new ResponseEntity<>(orderServiceResponse, HttpStatus.CREATED);
        } else {
            orderServiceResponse.setProcessingStatus(ProcessingStatus.FAILED);
            orderServiceResponse.setMessage(ProcessingStatus.FAILED.getMessage());
            return new ResponseEntity<>(orderServiceResponse, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
