package com.nimbleways.springboilerplate.utils;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductTypeEnum;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@UtilityClass
public class DataMock {
    public static List<Product> allProducts() {
        return List.of(

                Product.builder()
                        .available(22)
                        .leadTime(18)
                        .type(ProductTypeEnum.NORMAL.getValue())
                        .name("HDMI Cable")
                        .build(),

                Product.builder()
                        .available(7)
                        .leadTime(0)
                        .type(ProductTypeEnum.NORMAL.getValue())
                        .name("Wireless Mouse")
                        .build(),

                Product.builder()
                        .available(12)
                        .leadTime(25)
                        .type(ProductTypeEnum.EXPIRABLE.getValue())
                        .name("Cheese")
                        .expiryDate(LocalDate.now().plusDays(14))
                        .build(),

                Product.builder()
                        .available(55)
                        .leadTime(8)
                        .type(ProductTypeEnum.EXPIRABLE.getValue())
                        .name("Yogurt")
                        .expiryDate(LocalDate.now().minusDays(5))
                        .build(),

                Product.builder()
                        .available(9)
                        .leadTime(20)
                        .type(ProductTypeEnum.SEASONAL.getValue())
                        .name("Strawberry")
                        .seasonStartDate(LocalDate.now().minusDays(10))
                        .seasonEndDate(LocalDate.now().plusDays(40))
                        .build(),

                Product.builder()
                        .available(18)
                        .leadTime(35)
                        .type(ProductTypeEnum.SEASONAL.getValue())
                        .name("Pumpkin")
                        .seasonStartDate(LocalDate.now().plusDays(120))
                        .seasonEndDate(LocalDate.now().plusDays(200))
                        .build()
        );
    }

    public static Order order() {
        return Order.builder().id(1L).items(new HashSet<>(allProducts())).build();
    }
}
