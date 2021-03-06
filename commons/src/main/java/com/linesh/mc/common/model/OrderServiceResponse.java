package com.linesh.mc.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.linesh.mc.common.enums.ProcessingStatus;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderServiceResponse implements Serializable {
    private String orderId;
    private String customerId;
    private String message;
    private ProcessingStatus processingStatus;
}
