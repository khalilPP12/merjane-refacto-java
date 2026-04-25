package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.services.implementations.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderApiTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    @Test
    void processOrderTest_success() throws Exception {
        Long orderId = 1L;
        mockMvc.perform(
                        post("/orders/{orderId}/processOrder", orderId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        verify(orderService).processOrder(orderId);
    }
}
