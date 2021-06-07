package com.linesh.mc.jms;

import com.linesh.mc.model.ShippingUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShippingServiceDelegate {

    @Value("${shippingServiceQueueName}")
    private String shippingServiceQueueName;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void postMessageToQueue(ShippingUnit shippingUnit) {
        log.info("Sending message to {}...", shippingServiceQueueName);
        try {
            jmsTemplate.convertAndSend(shippingServiceQueueName, shippingUnit);
            log.info("Message sent to {} successfully!", shippingServiceQueueName);
        } catch (JmsException e) {
            log.error("Error when sending message to Shipping Service Queue - {}", e.getMessage());
        }
    }
}
