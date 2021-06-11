package com.linesh.mc.shipping.repository;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.linesh.mc.shipping.entities.ShippingUnitEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ShippingRepository extends ReactiveCosmosRepository<ShippingUnitEntity, String> {
    Flux<ShippingUnitEntity> findByShippingId(String shippingId);
}
