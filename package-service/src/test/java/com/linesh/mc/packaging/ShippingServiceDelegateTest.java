package com.linesh.mc.packaging;

import com.linesh.mc.common.model.ShippingUnit;
import com.linesh.mc.packaging.jms.ShippingServiceDelegate;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@SpringBootTest
class ShippingServiceDelegateTest {

    @InjectMocks
    ShippingServiceDelegate shippingServiceDelegate;

    @Mock
    JmsTemplate jmsTemplate;

    @Test
    void postMessageToQueueHappyPath() throws Exception {
        doNothing().when(jmsTemplate).convertAndSend(Mockito.anyString(), Mockito.any(ShippingUnit.class));
        ReflectionTestUtils.setField(shippingServiceDelegate, "shippingServiceQueueName", "mockQueueName");
        shippingServiceDelegate.postMessageToQueue(new ShippingUnit());
        verify(jmsTemplate, times(1)).convertAndSend(Mockito.anyString(), Mockito.any(ShippingUnit.class));
    }
}
