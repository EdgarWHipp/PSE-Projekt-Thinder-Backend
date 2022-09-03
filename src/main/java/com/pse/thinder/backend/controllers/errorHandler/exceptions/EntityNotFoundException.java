package com.pse.thinder.backend.controllers.errorHandler.exceptions;

/**
 * Simple Exception when an Entity could not be found in the database.
 *
 */
public class EntityNotFoundException extends RuntimeException{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5679131732661154857L;

	/**
	 * Creates a new Instance with the given error message
	 * @param message the error message
	 */
	public EntityNotFoundException(String message) {
        super(message);
    }
}
