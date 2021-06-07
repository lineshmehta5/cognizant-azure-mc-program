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

    public ResponseEntity<OrderServiceResponse> processOrder(OrderData orderData) {
        orderData.setOrderId(UUID.randomUUID().toString());


        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("externalService");

        for (int i = 0; i < 500; i++) {
            System.out.println("counter = " + (i + 1));

            try {
                // Call service every 1 second.
                Thread.sleep(1000);
                // Decorate service call in circuit breaker
                String status = circuitBreaker.executeSupplier(() -> externalService.callService());
                System.out.println(status);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Print important metric stats to observe behavior of circuit breaker.
                System.out.println("Successful call count: " + circuitBreaker.getMetrics().getNumberOfSuccessfulCalls()
                        + " | Failed call count: " + circuitBreaker.getMetrics().getNumberOfFailedCalls()
                        + " | Failure rate %:" + circuitBreaker.getMetrics().getFailureRate() + " | State: "
                        + circuitBreaker.getState());
            }
        }


        ResponseEntity<PackageServiceResponse> packageServiceResponseResponseEntity = packageServiceDelegate.sortPackage(orderData);


        OrderServiceResponse orderServiceResponse = new OrderServiceResponse();
        orderServiceResponse.setOrderId(orderData.getOrderId());
        orderServiceResponse.setCustomerId(orderData.getCustomerId());
        if (packageServiceResponseResponseEntity.getStatusCode().is2xxSuccessful()) {
            orderServiceResponse.setProcessingStatus(ProcessingStatus.PROCESSED);
            orderServiceResponse.setMessage(ProcessingStatus.PROCESSED.getMessage());
            return new ResponseEntity<>(orderServiceResponse, HttpStatus.CREATED);
        } else {
            orderServiceResponse.setProcessingStatus(ProcessingStatus.FAILED);
            orderServiceResponse.setMessage(ProcessingStatus.FAILED.getMessage());
            return new ResponseEntity<>(orderServiceResponse, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
