package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    @Transactional

    public void processOrder(Long orderId) {
        log.debug("Start service: process with id '{}'", orderId);
        final var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        productService.processProductOrder(order);
        log.debug("End service: process with id '{}'", orderId);
    }
}
