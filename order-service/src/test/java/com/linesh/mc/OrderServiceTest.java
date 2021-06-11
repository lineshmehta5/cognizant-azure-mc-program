package com.linesh.mc;

import com.linesh.mc.jms.PackageServiceDelegate;
import com.linesh.mc.model.OrderData;
import com.linesh.mc.model.OrderServiceResponse;
import com.linesh.mc.model.PackageServiceResponse;
import com.linesh.mc.service.OrderService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    PackageServiceDelegate packageServiceDelegate;

    @Mock
    CircuitBreaker circuitBreaker;

    @Mock
    CircuitBreaker.Metrics mockMetrics;

    @Test
    void processServiceHappyPath() throws Exception {
        ResponseEntity<PackageServiceResponse> packageServiceResponseResponseEntity = new ResponseEntity<>(new PackageServiceResponse(), HttpStatus.CREATED);
        when(circuitBreaker.executeSupplier(Mockito.any())).thenReturn(packageServiceResponseResponseEntity);
        when(circuitBreaker.getMetrics()).thenReturn(mockMetrics);
        when(mockMetrics.getNumberOfSuccessfulCalls()).thenReturn(1);
        when(mockMetrics.getNumberOfFailedCalls()).thenReturn(1);
        when(mockMetrics.getFailureRate()).thenReturn(1f);
        when(packageServiceDelegate.sortPackage(Mockito.any())).thenReturn(packageServiceResponseResponseEntity);
        ResponseEntity<OrderServiceResponse> orderServiceResponseResponseEntity = orderService.processOrder(new OrderData());
        assertThat(orderServiceResponseResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    }

    @Test
    void processServiceFallbackPath() throws Exception {
        ResponseEntity<PackageServiceResponse> packageServiceResponseResponseEntity = new ResponseEntity<>(new PackageServiceResponse(), HttpStatus.EXPECTATION_FAILED);
        when(circuitBreaker.executeSupplier(Mockito.any())).thenReturn(packageServiceResponseResponseEntity);
        when(circuitBreaker.getMetrics()).thenReturn(mockMetrics);
        when(mockMetrics.getNumberOfSuccessfulCalls()).thenReturn(1);
        when(mockMetrics.getNumberOfFailedCalls()).thenReturn(1);
        when(mockMetrics.getFailureRate()).thenReturn(1f);
        when(packageServiceDelegate.sortPackage(Mockito.any())).thenReturn(packageServiceResponseResponseEntity);
        ResponseEntity<OrderServiceResponse> orderServiceResponseResponseEntity = orderService.processOrder(new OrderData());
        assertThat(orderServiceResponseResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.EXPECTATION_FAILED);
    }
}
