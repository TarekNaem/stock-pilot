package com.stock.pilot.exception;

public class InsufficientStockException extends BusinessException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
