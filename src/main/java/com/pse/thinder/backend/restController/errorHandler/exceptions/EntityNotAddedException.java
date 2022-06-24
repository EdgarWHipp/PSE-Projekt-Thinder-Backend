package com.pse.thinder.backend.restController.errorHandler.exceptions;

public class EntityNotAddedException extends RuntimeException {
    public EntityNotAddedException(String message) {
        super(message);
    }
}
