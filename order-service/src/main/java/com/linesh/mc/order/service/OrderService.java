package com.linesh.mc.order.service;

import com.linesh.mc.common.enums.ProcessingStatus;
import com.linesh.mc.common.model.OrderData;
import com.linesh.mc.common.model.OrderServiceResponse;
import com.linesh.mc.common.model.PackageServiceResponse;
import com.linesh.mc.order.jms.PackageServiceDelegate;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private PackageServiceDelegate packageServiceDelegate;

    public ResponseEntity<OrderServiceResponse> processOrder(OrderData orderData) {
        String orderId = UUID.randomUUID().toString();
        orderData.setOrderId(orderId);
        log.info("Started Processing new Order Id {}", orderId);
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .minimumNumberOfCalls(5)//Circuit Breaker will start checking failures after 5 calls are made
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("packageService");

        circuitBreaker.getEventPublisher().onStateTransition(e -> log.info(e.toString()));
        circuitBreaker.getEventPublisher().onError(e -> log.error(e.toString()));

        Supplier<ResponseEntity<PackageServiceResponse>> packageServiceSupplier = () -> packageServiceDelegate.sortPackage(orderData);
        Supplier<ResponseEntity<PackageServiceResponse>> decorated = Decorators
                .ofSupplier(packageServiceSupplier)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(Arrays.asList(Exception.class), e -> packageServiceDelegate.sortPackageFallback(orderData))
                .decorate();
        log.info("Circuit Breaker Statistics :: Successful call count: {}  | Failed call count: {} | Failure rate %: {}  | State: {}", circuitBreaker.getMetrics().getNumberOfSuccessfulCalls(), circuitBreaker.getMetrics().getNumberOfFailedCalls(), circuitBreaker.getMetrics().getFailureRate(), circuitBreaker.getState());
        return convertToOrderResponse(decorated.get(), orderData);
    }

    private ResponseEntity<OrderServiceResponse> convertToOrderResponse(ResponseEntity<PackageServiceResponse> packageServiceResponseResponseEntity, OrderData orderData) {
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
