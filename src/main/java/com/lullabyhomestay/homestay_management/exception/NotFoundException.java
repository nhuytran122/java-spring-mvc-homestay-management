package com.lullabyhomestay.homestay_management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotFoundException extends RuntimeException {
    private String entityName;

    public NotFoundException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
    }
}