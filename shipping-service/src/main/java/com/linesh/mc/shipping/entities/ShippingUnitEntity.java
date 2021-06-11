package com.linesh.mc.shipping.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.linesh.mc.common.model.ItemData;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Data
@Container(containerName = "shippingunit")
public class ShippingUnitEntity implements Serializable {
    @Id
    private String shippingId;
    @PartitionKey
    private String orderId;
    private List<ItemData> itemDataList;
}
