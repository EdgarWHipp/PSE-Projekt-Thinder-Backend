package com.pse.thinder.backend.controllers.errorHandler.exceptions;

public class EntityNotAddedException extends RuntimeException {
    public EntityNotAddedException(String message) {
        super(message);
    }
}
