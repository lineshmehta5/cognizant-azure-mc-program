package com.linesh.mc.service;

import com.linesh.mc.entities.ShippingUnitEntity;
import com.linesh.mc.model.ShippingUnit;
import com.linesh.mc.repository.ShippingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ShippingService {

    @Autowired
    ShippingRepository shippingRepository;

    public void persist(ShippingUnit shippingUnit) {
        ShippingUnitEntity shippingUnitEntity = new ShippingUnitEntity();
        BeanUtils.copyProperties(shippingUnit, shippingUnitEntity);
        Mono<ShippingUnitEntity> shippingUnitEntityMono = shippingRepository.save(shippingUnitEntity);
        shippingUnitEntityMono.block();
        log.info("Persisted Shipping Unit {} to Database", shippingUnit.getShippingId());
    }
}
