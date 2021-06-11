package com.linesh.mc.shipping;

import com.linesh.mc.common.model.ShippingUnit;
import com.linesh.mc.shipping.jms.QueueListener;
import com.linesh.mc.shipping.service.ShippingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@SpringBootTest
class QueueListenerTest {

    @InjectMocks
    QueueListener queueListener;

    @Mock
    ShippingService shippingService;

    @Test
    void callShippingServiceOnRecievingMessage() throws Exception {
        doNothing().when(shippingService).persist(Mockito.any());
        queueListener.receiveMessage(new ShippingUnit());
        verify(shippingService, times(1)).persist(Mockito.any());
    }
}
