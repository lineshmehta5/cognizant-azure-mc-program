package com.linesh.mc.jms;

import com.linesh.mc.model.ShippingUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShippingServiceQueueListener {

    @JmsListener(destination = "${shippingServiceQueueName}", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(ShippingUnit shippingUnit) {
        log.info("Received Shipping Unit with id {}", shippingUnit.getShippingId());
    }
}
