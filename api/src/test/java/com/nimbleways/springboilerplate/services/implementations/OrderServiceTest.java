package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import com.nimbleways.springboilerplate.utils.DataMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@UnitTest
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    ProductService productService;
    @InjectMocks
    OrderService orderService;

    @Test
    void processOrder_shouldProcessOrder_whenOrderExists() {
        final var order = DataMock.order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        orderService.processOrder(1L);

        verify(orderRepository, times(1)).findById(1L);
        verify(productService, times(1)).processProductOrder(order);
    }

    @Test
    void processOrder_shouldThrowException_whenOrderDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class, () -> orderService.processOrder(1L));
        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository, times(1)).findById(1L);
        verifyNoInteractions(productService);
    }
}