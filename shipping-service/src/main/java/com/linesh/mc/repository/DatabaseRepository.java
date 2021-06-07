package com.linesh.mc.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.linesh.mc.entity.ShippingUnitEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseRepository extends CosmosRepository<ShippingUnitEntity, String> {
}