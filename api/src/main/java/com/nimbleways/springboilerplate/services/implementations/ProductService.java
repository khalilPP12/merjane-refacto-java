package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    public void processProductOrder(Order order) {
        log.debug("Start service: processing product order with id '{}'", order.getId());
        for (var product : order.getItems()) {
            switch (product.getProductType()) {
                case NORMAL -> processNormal(product);
                case SEASONAL -> processSeasonal(product);
                case EXPIRABLE -> processExpirable(product);
            }
        }
        log.debug("End service: processing product order with id '{}'", order.getId());
    }

    private void decrementStock(Product product) {
        product.setAvailable(product.getAvailable() - 1);
        productRepository.save(product);
    }

    private void processNormal(Product product) {
        if (product.getAvailable() > 0) {
            decrementStock(product);
        } else {
            notifyDelay(product.getLeadTime(), product);
        }
    }

    private void processSeasonal(Product product) {
        final var today = LocalDate.now();
        final var inSeason = !today.isBefore(product.getSeasonStartDate()) &&
                !today.isAfter(product.getSeasonEndDate());
        if (inSeason && product.getAvailable() > 0) {
            decrementStock(product);
            return;
        }

        final var restockDate = today.plusDays(product.getLeadTime());

        if (restockDate.isAfter(product.getSeasonEndDate())) {
            notificationService.sendOutOfStockNotification(product.getName());
        } else {
            notifyDelay(product.getLeadTime(), product);
        }
    }

    private void processExpirable(Product product) {
        final var today = LocalDate.now();
        if (product.getAvailable() > 0 && today.isBefore(product.getExpiryDate())) {
            decrementStock(product);
        } else {
            handleExpiredProduct(product);
        }
    }

    public void notifyDelay(int leadTime, Product p) {
        if (leadTime > 0) {
            p.setLeadTime(leadTime);
            productRepository.save(p);
            notificationService.sendDelayNotification(leadTime, p.getName());
        }
    }

    public void handleExpiredProduct(Product p) {
        if (p.getAvailable() > 0 && p.getExpiryDate().isAfter(LocalDate.now())) {
            p.setAvailable(p.getAvailable() - 1);
            productRepository.save(p);
        } else {
            notificationService.sendExpirationNotification(p.getName(), p.getExpiryDate());
            p.setAvailable(0);
            productRepository.save(p);
        }
    }
}