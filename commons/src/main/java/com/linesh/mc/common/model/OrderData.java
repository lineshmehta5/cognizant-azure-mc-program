package com.linesh.mc.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderData implements Serializable {
    private String customerId;
    private String orderId;
    private List<ItemData> itemDataList;
}
