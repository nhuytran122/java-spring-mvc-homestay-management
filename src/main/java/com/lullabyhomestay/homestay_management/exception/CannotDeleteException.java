package com.lullabyhomestay.homestay_management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CannotDeleteException extends RuntimeException {
    private String entityName;

    public CannotDeleteException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
    }
}
