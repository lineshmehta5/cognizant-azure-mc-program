package com.linesh.mc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linesh.mc.enums.ProcessingStatus;
import com.linesh.mc.model.OrderData;
import com.linesh.mc.model.OrderServiceResponse;
import com.linesh.mc.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    OrderService orderService;

    @Test
    void createOrderShouldCreateOrderHappyPath() throws Exception {
        ResponseEntity<OrderServiceResponse> mockResponse = new ResponseEntity<>(getMockOrderServiceResponseProcessed(), HttpStatus.CREATED);
        when(orderService.processOrder(Mockito.any(OrderData.class))).thenReturn(mockResponse);
        mvc.perform(MockMvcRequestBuilders
                .post("/create-order")
                .content(asJsonString(new OrderData()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void createOrderShouldCreateOrderFallBackPath() throws Exception {
        ResponseEntity<OrderServiceResponse> mockResponse = new ResponseEntity<>(getMockOrderServiceResponseFailed(), HttpStatus.EXPECTATION_FAILED);
        when(orderService.processOrder(Mockito.any(OrderData.class))).thenReturn(mockResponse);
        mvc.perform(MockMvcRequestBuilders
                .post("/create-order")
                .content(asJsonString(new OrderData()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isExpectationFailed());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private OrderServiceResponse getMockOrderServiceResponseProcessed() {
        OrderServiceResponse orderServiceResponse = new OrderServiceResponse();
        orderServiceResponse.setOrderId("mockOrderId");
        orderServiceResponse.setMessage(ProcessingStatus.PROCESSED.getMessage());
        orderServiceResponse.setCustomerId("mockCustomerId");
        orderServiceResponse.setProcessingStatus(ProcessingStatus.PROCESSED);
        return orderServiceResponse;
    }

    private OrderServiceResponse getMockOrderServiceResponseFailed() {
        OrderServiceResponse orderServiceResponse = new OrderServiceResponse();
        orderServiceResponse.setOrderId("mockOrderId");
        orderServiceResponse.setMessage(ProcessingStatus.FAILED.getMessage());
        orderServiceResponse.setCustomerId("mockCustomerId");
        orderServiceResponse.setProcessingStatus(ProcessingStatus.FAILED);
        return orderServiceResponse;
    }
}
