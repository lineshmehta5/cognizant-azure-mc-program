package com.linesh.mc.jms;

import com.linesh.mc.model.ItemData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CourierServiceQueueListener {

    @Value("${courierServiceQueueName}")
    private String courierServiceQueueName;

    @JmsListener(destination = "${courierServiceQueueName}", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(List<ItemData> toBeShippedItems) {
        log.info("Received {} items which have to shipped!", toBeShippedItems.size());
    }

}
