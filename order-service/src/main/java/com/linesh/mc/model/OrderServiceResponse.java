package com.linesh.mc.model;

import com.linesh.mc.enums.OrderProcessingStatus;
import lombok.Data;

@Data
public class OrderServiceResponse {
    private String orderId;
    private String customerId;
    private String message;
    private OrderProcessingStatus orderProcessingStatus;
}
