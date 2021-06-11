package com.linesh.mc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.linesh.mc.enums.ProcessingStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageServiceResponse implements Serializable {
    private String orderId;
    private String customerId;
    private String message;
    private ProcessingStatus processingStatus;
    private List<ItemData> itemDataList;
}
