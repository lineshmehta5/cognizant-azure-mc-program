package com.linesh.mc.jms;

import com.linesh.mc.enums.ProcessingStatus;
import com.linesh.mc.model.OrderData;
import com.linesh.mc.model.PackageServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class PackageServiceDelegate {

    @Value("${packageServiceSortPackageEndpointUrl}")
    private String packageServiceSortPackageEndpointUrl;

    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ResponseEntity<PackageServiceResponse> sortPackage(OrderData orderData) {
        log.info("Calling Package Service...");
        return new RestTemplate().postForEntity(packageServiceSortPackageEndpointUrl, orderData, PackageServiceResponse.class);
    }

    @Recover
    public ResponseEntity<PackageServiceResponse> sortPackageFallback(RuntimeException e, OrderData orderData) {
        log.info("Inside Fallback for Package Service...");
        PackageServiceResponse packageServiceResponse = new PackageServiceResponse();
        packageServiceResponse.setOrderId(orderData.getOrderId());
        packageServiceResponse.setProcessingStatus(ProcessingStatus.FAILED);
        packageServiceResponse.setMessage(ProcessingStatus.FAILED.getMessage());
        log.info("Returning Response from Fallback for Package Service...");
        return new ResponseEntity<>(new PackageServiceResponse(), HttpStatus.EXPECTATION_FAILED);
    }
}
