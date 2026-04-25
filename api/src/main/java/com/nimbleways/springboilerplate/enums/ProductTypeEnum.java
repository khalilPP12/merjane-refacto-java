package com.nimbleways.springboilerplate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ProductTypeEnum {
    NORMAL("NORMAL"),
    SEASONAL("SEASONAL"),
    EXPIRABLE("EXPIRABLE");

    private final String value;

    public static ProductTypeEnum getTypeFromValue(String value) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unknown product type: " + value)
                );
    }
}
