package com.pse.thinder.backend.controllers.errorHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotAddedException;
import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;

/**
 * This class is used by Spring Boot to handle Exceptions while executing rest controller mappings for
 * a http request. This is needed because every exception result to an 500 status code by default
 *
 */
@ControllerAdvice
public class GlobalContollerAdvice extends ResponseEntityExceptionHandler{

	/**
	 * This method handles EntityNotAddedException so that they result in a conflict status code
	 * @param ex the exceptions
	 * @param request the request where the exception occurred
	 * @return the response which should be returned to the user
	 */
	@ExceptionHandler(EntityNotAddedException.class)
    public ResponseEntity<Object> handleEntityNotAddedException(EntityNotAddedException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
	
	/**
	 * This method handles EntityNotFoundException so that they result in a not found status code
	 * @param ex the exceptions
	 * @param request the request where the exception occurred
	 * @return the response which should be returned to the user
	 */
	@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
	
	/**
	 * This method handles IllegalArgumentException so that they result in a bad request status code
	 * @param ex the exceptions
	 * @param request the request where the exception occurred
	 * @return the response which should be returned to the user
	 */
	@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
	
}
