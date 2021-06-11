package com.linesh.mc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class ShippingUnit implements Serializable {
    private String shippingId;
    private String orderId;
    private List<ItemData> itemDataList;
}
