package com.linesh.mc.model;

import lombok.Data;

@Data
public class ItemData {
    private long itemId;
    private String name;
    private int quantity;
    private int unitPrice;
    private long totalCost;
    private AddressData addressData;
}
