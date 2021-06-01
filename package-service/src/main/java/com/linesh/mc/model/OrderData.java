package com.linesh.mc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class OrderData {
    private String customerId;
    @JsonIgnore
    private String orderId;
    private List<ItemData> itemDataList;
}
