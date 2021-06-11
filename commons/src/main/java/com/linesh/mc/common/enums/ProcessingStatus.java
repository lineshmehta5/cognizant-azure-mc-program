package com.linesh.mc.common.enums;

import lombok.Getter;

public enum ProcessingStatus {
    PROCESSED("Order has been successfully processed!"),
    FAILED("Order processing Failed!");

    @Getter
    private String message;

    private ProcessingStatus(String message) {
        this.message = message;
    }
}
