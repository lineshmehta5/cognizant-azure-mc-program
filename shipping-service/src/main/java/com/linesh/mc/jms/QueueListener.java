package com.linesh.mc.jms;

import com.linesh.mc.model.ShippingUnit;
import com.linesh.mc.service.ShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueListener {

    @Autowired
    private ShippingService shippingService;

    @JmsListener(destination = "${shippingServiceQueueName}", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(ShippingUnit shippingUnit) {
        log.info("Received Shipping Unit with id {}", shippingUnit.getShippingId());
        shippingService.persist(shippingUnit);
    }
}
