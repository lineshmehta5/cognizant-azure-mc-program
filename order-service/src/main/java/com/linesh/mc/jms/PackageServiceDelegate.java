package com.linesh.mc.jms;

import com.linesh.mc.enums.ProcessingStatus;
import com.linesh.mc.model.OrderData;
import com.linesh.mc.model.PackageServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class PackageServiceDelegate {

    @Value("${packageServiceSortPackageEndpointUrl}")
    private String packageServiceSortPackageEndpointUrl;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<PackageServiceResponse> sortPackage(OrderData orderData) {
        log.info("Calling Package Service @ {}", packageServiceSortPackageEndpointUrl);
        return restTemplate.postForEntity(packageServiceSortPackageEndpointUrl, orderData, PackageServiceResponse.class);
    }

    public ResponseEntity<PackageServiceResponse> sortPackageFallback(OrderData orderData) {
        log.info("Inside Fallback for Package Service...");
        PackageServiceResponse packageServiceResponse = new PackageServiceResponse();
        packageServiceResponse.setOrderId(orderData.getOrderId());
        packageServiceResponse.setProcessingStatus(ProcessingStatus.FAILED);
        packageServiceResponse.setMessage(ProcessingStatus.FAILED.getMessage());
        log.info("Returning Response from Fallback for Package Service...");
        return new ResponseEntity<>(new PackageServiceResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
