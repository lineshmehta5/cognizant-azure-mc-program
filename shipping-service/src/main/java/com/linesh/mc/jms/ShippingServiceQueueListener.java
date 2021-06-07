package com.linesh.mc.jms;

import com.linesh.mc.entity.ShippingUnitEntity;
import com.linesh.mc.model.ShippingUnit;
import com.linesh.mc.repository.DatabaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShippingServiceQueueListener {

    @Value("${shippingServiceQueueName}")
    private String shippingServiceQueueName;

    @Autowired
    private DatabaseRepository databaseRepository;

    @JmsListener(destination = "${shippingServiceQueueName}", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(ShippingUnit shippingUnit) {
        log.info("Received Shipping Unit with id {}", shippingUnit.getShippingId());
        databaseRepository.save(convertToEntity(shippingUnit));
        log.info("Saved to DB with id {}", shippingUnit.getShippingId());
    }

    private ShippingUnitEntity convertToEntity(ShippingUnit shippingUnit) {
        ShippingUnitEntity shippingUnitEntity = new ShippingUnitEntity();
        shippingUnitEntity.setShippingId(shippingUnit.getShippingId());
        shippingUnitEntity.setItemDataList(shippingUnit.getItemDataList());
        shippingUnitEntity.setOrderId(shippingUnit.getOrderId());
        return shippingUnitEntity;
    }
}
