package com.linesh.mc.packaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linesh.mc.common.model.OrderData;
import com.linesh.mc.packaging.service.PackageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PackageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    PackageService packageService;

    @Test
    void sortPackageShouldSortPackage() throws Exception {
        doNothing().when(packageService).sortAndSendPackages(Mockito.any(OrderData.class));
        mvc.perform(MockMvcRequestBuilders
                .post("/sort-package")
                .content(asJsonString(new OrderData()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
