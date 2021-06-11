package com.linesh.mc.shipping;

import com.linesh.mc.common.model.ShippingUnit;
import com.linesh.mc.shipping.entities.ShippingUnitEntity;
import com.linesh.mc.shipping.repository.ShippingRepository;
import com.linesh.mc.shipping.service.ShippingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@SpringBootTest
class ShippingServiceTest {

    @InjectMocks
    ShippingService shippingService;

    @Mock
    ShippingRepository shippingRepository;

    @Test
    void persistHappyPath() throws Exception {
        when(shippingRepository.save(Mockito.any())).thenReturn(Mono.just(new ShippingUnitEntity()));
        shippingService.persist(new ShippingUnit());
        verify(shippingRepository, times(1)).save(Mockito.any(ShippingUnitEntity.class));
    }
}
