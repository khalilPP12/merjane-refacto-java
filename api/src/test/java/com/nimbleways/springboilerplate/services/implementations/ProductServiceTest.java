package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductTypeEnum;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import com.nimbleways.springboilerplate.utils.DataMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

import java.time.LocalDate;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@UnitTest
public class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    NotificationService notificationService;

    @InjectMocks
    ProductService productService;

    @Test
    void processProductOrder_shouldProcessAllProducts() {
        productService.processProductOrder(DataMock.order());
        verify(productRepository, times(2)).save(argThat(p -> ProductTypeEnum.NORMAL.equals(p.getProductType())));
        verify(productRepository, times(2)).save(argThat(p -> ProductTypeEnum.SEASONAL.equals(p.getProductType())));
        verify(productRepository, times(2)).save(argThat(p -> ProductTypeEnum.EXPIRABLE.equals(p.getProductType())));

    }

    @Test
    void processNormal_shouldNotifyDelay_whenOutOfStock() {
        var emptyProduct = Product.builder().build();
        var normalProduct = DataMock.allProducts().stream()
                .filter(product -> ProductTypeEnum.NORMAL.equals(product.getProductType()))
                .findFirst().orElse(emptyProduct);
        normalProduct.setAvailable(0);

        productService.processProductOrder(new Order(2L, Set.of(normalProduct)));

        verify(productRepository, times(1)).save(normalProduct);
        verify(notificationService, times(1))
                .sendDelayNotification(normalProduct.getLeadTime(), normalProduct.getName());
    }

    @Test
    void processSeasonal_shouldSendOutOfStockNotification_whenRestockAfterSeason() {
        var emptyProduct = Product.builder().build();
        var seasonalProduct = DataMock.allProducts().stream()
                .filter(product -> ProductTypeEnum.SEASONAL.equals(product.getProductType()))
                .findFirst().orElse(emptyProduct);
        seasonalProduct.setSeasonEndDate(LocalDate.now().plusDays(2));
        seasonalProduct.setLeadTime(10);
        seasonalProduct.setAvailable(0);

        productService.processProductOrder(new Order(3L, Set.of(seasonalProduct)));

        verify(notificationService, times(1)).sendOutOfStockNotification(seasonalProduct.getName());
    }

    @Test
    void processExpirable_shouldSendExpirationNotification_whenExpired() {
        var emptyProduct = Product.builder().build();
        var expirableProduct = DataMock.allProducts().stream()
                .filter(product -> ProductTypeEnum.EXPIRABLE.equals(product.getProductType()))
                .findFirst().orElse(emptyProduct);
        expirableProduct.setExpiryDate(LocalDate.now().minusDays(1));
        expirableProduct.setAvailable(1);

        productService.processProductOrder(new Order(4L, Set.of(expirableProduct)));

        verify(notificationService, times(1))
                .sendExpirationNotification(expirableProduct.getName(), expirableProduct.getExpiryDate());
        assertEquals(0, expirableProduct.getAvailable());
        verify(productRepository, times(1)).save(expirableProduct);
    }
}
