package com.linesh.mc.shipping;

import com.linesh.mc.shipping.service.ShippingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ShippingServiceApplicationTests {

    @Autowired
    ShippingService shippingService;

    @Test
    void contextLoads() {
        assertThat(shippingService).isNotNull();
    }

}
