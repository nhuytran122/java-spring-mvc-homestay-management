package com.lullabyhomestay.homestay_management.exception;

import com.lullabyhomestay.homestay_management.domain.InventoryTransaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryException extends RuntimeException {
    private final InventoryTransaction data;

    public InventoryException(String message, InventoryTransaction data) {
        super(message);
        this.data = data;
    }
}