package com.pse.thinder.backend.controllers.errorHandler.exceptions;

/**
 * Simple Exception when an Entity could not be added to the database.
 *
 */
public class EntityNotAddedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1676531803159544360L;

	/**
	 * Creates a new Instance with the given error message
	 * @param message the error message
	 */
	public EntityNotAddedException(String message) {
        super(message);
    }
}
