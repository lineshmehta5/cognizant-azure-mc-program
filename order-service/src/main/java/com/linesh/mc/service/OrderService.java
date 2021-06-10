package com.linesh.mc.service;

import com.linesh.mc.enums.ProcessingStatus;
import com.linesh.mc.jms.PackageServiceDelegate;
import com.linesh.mc.model.OrderData;
import com.linesh.mc.model.OrderServiceResponse;
import com.linesh.mc.model.PackageServiceResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private PackageServiceDelegate packageServiceDelegate;

    CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("packageService");

    public ResponseEntity<OrderServiceResponse> processOrder(OrderData orderData) {
        String orderId = UUID.randomUUID().toString();
        orderData.setOrderId(orderId);
        log.info("Started Processing new Order Id {}", orderId);
        ResponseEntity<PackageServiceResponse> packageServiceResponseResponseEntity = new ResponseEntity<>(new PackageServiceResponse(), HttpStatus.NO_CONTENT);
        try {
            packageServiceResponseResponseEntity = circuitBreaker.executeSupplier(() -> packageServiceDelegate.sortPackage(orderData));
        } catch (Exception e) {
            log.error("{} when calling PackageService", e.getMessage());
        } finally {
            // Print important metric stats to observe behavior of circuit breaker.
            log.info("Circuit Breaker Statistics :: Successful call count: {}  | Failed call count: {} | Failure rate %: {}  | State: {}", circuitBreaker.getMetrics().getNumberOfSuccessfulCalls(), circuitBreaker.getMetrics().getNumberOfFailedCalls(), circuitBreaker.getMetrics().getFailureRate(), circuitBreaker.getState());
        }
        OrderServiceResponse orderServiceResponse = new OrderServiceResponse();
        orderServiceResponse.setOrderId(orderData.getOrderId());
        orderServiceResponse.setCustomerId(orderData.getCustomerId());
        if (packageServiceResponseResponseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("Order Processing Completed Successfully in Package Service");
            orderServiceResponse.setProcessingStatus(ProcessingStatus.PROCESSED);
            orderServiceResponse.setMessage(ProcessingStatus.PROCESSED.getMessage());
            return new ResponseEntity<>(orderServiceResponse, HttpStatus.CREATED);
        } else {
            log.error("Order Processing was unsuccessful in Package Service");
            orderServiceResponse.setProcessingStatus(ProcessingStatus.FAILED);
            orderServiceResponse.setMessage(ProcessingStatus.FAILED.getMessage());
            return new ResponseEntity<>(orderServiceResponse, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
