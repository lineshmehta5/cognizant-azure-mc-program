package com.linesh.mc.service;

import com.linesh.mc.model.OrderData;
import com.linesh.mc.model.OrderServiceResponse;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PackageService {

    @Value("${packageServiceCreateOrderEndpointUrl}")
    private String packageServiceCreateOrderEndpointUrl;

    @Retryable(value = {Exception.class}, maxAttemptsExpression = "#{${package-service-retry.maxAttempts}}", backoff = @Backoff(delayExpression = "#{${package-service-retry.delayExpression}}"))
    public Mono<OrderServiceResponse> sendToPackageService(OrderData orderData) {
        WebClient webClient = getSimpleWebClient();
        Mono<OrderServiceResponse> createdOrder = webClient.post()
                //.uri("/create-order")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(orderData), OrderData.class)
                .retrieve()
                .bodyToMono(OrderServiceResponse.class)
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(2)))
                .onErrorResume(e -> sendToPackageServiceFallback());
        return createdOrder;
    }

    @Recover
    public Mono<OrderServiceResponse> sendToPackageServiceFallback() {
        log.info("Inside Fallback");
        OrderServiceResponse orderServiceResponse = new OrderServiceResponse();
        orderServiceResponse.setCustomerId("From Fallback Method");
        return Mono.just(orderServiceResponse);
    }

    private WebClient getSimpleWebClient() {
        return WebClient.create(packageServiceCreateOrderEndpointUrl);
    }

    private WebClient getCustomWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
        return WebClient.builder()
                .baseUrl(packageServiceCreateOrderEndpointUrl)
                //.defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                //.defaultUriVariables(Collections.singletonMap("url", packageServiceEndpointUrl))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

}
