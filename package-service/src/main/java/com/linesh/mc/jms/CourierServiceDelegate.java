package com.linesh.mc.jms;

import com.linesh.mc.model.ItemData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CourierServiceDelegate {

    @Value("${courierServiceQueueName}")
    private String courierServiceQueueName;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void postMessageToQueue(List<ItemData> toBeShippedItems) {
        log.info("Sending message to {}...", courierServiceQueueName);
        try {
            jmsTemplate.convertAndSend(courierServiceQueueName, toBeShippedItems);
            log.info("Message sent to {} successfully!", courierServiceQueueName);
        } catch (JmsException e) {
            log.error("Error when sending message to Courier Service Queue - {}", e.getMessage());
        }
    }

}
