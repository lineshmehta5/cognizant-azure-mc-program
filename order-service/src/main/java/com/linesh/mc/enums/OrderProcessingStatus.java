package com.linesh.mc.enums;

import lombok.Getter;

public enum OrderProcessingStatus {
    PROCESSED("Order has been successfully processed"),
    FAILED_INVALID_ORDER("Order Validation failed as critical information was missing or incorrect");

    @Getter
    private String message;

    private OrderProcessingStatus(String message) {
        this.message = message;
    }
}
