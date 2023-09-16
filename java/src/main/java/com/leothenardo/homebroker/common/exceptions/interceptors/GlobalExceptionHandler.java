package com.leothenardo.homebroker.common.exceptions.interceptors;

import com.leothenardo.homebroker.common.exceptions.EntityValidationException;
import com.leothenardo.homebroker.common.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<GlobalCustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		var status = HttpStatus.NOT_FOUND;
		var error = new GlobalCustomError(
						status.value(),
						e.getMessage(),
						request.getRequestURI()
		);
		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GlobalCustomError> validationDtoException(MethodArgumentNotValidException e, HttpServletRequest request) {
		var status = HttpStatus.UNPROCESSABLE_ENTITY;
		var error = new GlobalCustomError(
						status.value(),
						e.getMessage(),
						request.getRequestURI(),
						e.getFieldErrors().stream().map(x -> x.getField() + ": " + x.getDefaultMessage()).toList()
		);
		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(EntityValidationException.class)
	public ResponseEntity<GlobalCustomError> validationEntityException(EntityValidationException e, HttpServletRequest request) {
		var status = HttpStatus.UNPROCESSABLE_ENTITY;
		var error = new GlobalCustomError(
						status.value(),
						e.getMessage(),
						request.getRequestURI(),
						e.getValidationErrors()
		);
		return ResponseEntity.status(status).body(error);
	}


}