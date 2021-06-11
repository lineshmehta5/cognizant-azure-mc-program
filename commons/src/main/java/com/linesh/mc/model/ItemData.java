package com.linesh.mc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemData implements Serializable {
    private String orderId;
    private long itemId;
    private String name;
    private int quantity;
    private int unitPrice;
    private long totalCost;
    private AddressData addressData;
}
