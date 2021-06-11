package com.linesh.mc;

import com.linesh.mc.jms.PackageServiceDelegate;
import com.linesh.mc.model.OrderData;
import com.linesh.mc.model.PackageServiceResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class PackageServiceDelegateTest {

    @InjectMocks
    PackageServiceDelegate packageServiceDelegate;

    @Mock
    RestTemplate restTemplate;

    @Test
    void sortPackageHappyPath() {
        OrderData orderData = new OrderData();
        when(restTemplate.postForEntity("someUrl", orderData, PackageServiceResponse.class)).thenReturn(new ResponseEntity<>(new PackageServiceResponse(), HttpStatus.CREATED));
        ReflectionTestUtils.setField(packageServiceDelegate, "packageServiceSortPackageEndpointUrl", "someUrl");
        ResponseEntity<PackageServiceResponse> response = packageServiceDelegate.sortPackage(orderData);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    }

    @Test
    void sortPackageFallback() {
        OrderData orderData = new OrderData();
        when(restTemplate.postForEntity("someUrl", orderData, PackageServiceResponse.class)).thenReturn(new ResponseEntity<>(new PackageServiceResponse(), HttpStatus.EXPECTATION_FAILED));
        ReflectionTestUtils.setField(packageServiceDelegate, "packageServiceSortPackageEndpointUrl", "someUrl");
        ResponseEntity<PackageServiceResponse> response = packageServiceDelegate.sortPackage(orderData);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.EXPECTATION_FAILED);
    }


}
