package com.nimbleways.springboilerplate.contollers;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.services.implementations.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderApi {
    private final OrderService orderService;

    @PostMapping("{orderId}/processOrder")
    @ResponseStatus(HttpStatus.OK)
    public ProcessOrderResponse processOrder(@PathVariable Long orderId) {
        log.debug("Start API: process order id '{}'", orderId);
        orderService.processOrder(orderId);
        final var output = new ProcessOrderResponse(orderId);
        log.debug("End API: processing order id '{}'", orderId);
        return output;
    }
}