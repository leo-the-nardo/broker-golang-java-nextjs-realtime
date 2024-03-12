package com.leothenardo.homebroker._common.exceptions.interceptors;

import com.leothenardo.homebroker._common.exceptions.AlreadyExistsException;
import com.leothenardo.homebroker._common.exceptions.EntityValidationException;
import com.leothenardo.homebroker._common.exceptions.ResourceNotFoundException;
import com.leothenardo.homebroker._common.exceptions.UnauthorizedException;
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

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<GlobalCustomError> unauthorized(UnauthorizedException e, HttpServletRequest request) {
		var status = HttpStatus.UNAUTHORIZED;
		var error = new GlobalCustomError(
						status.value(),
						e.getMessage(),
						request.getRequestURI()
		);
		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(AlreadyExistsException.class)
	public ResponseEntity<GlobalCustomError> resourceNotFound(AlreadyExistsException e, HttpServletRequest request) {
		var status = HttpStatus.CONFLICT;
		var error = new GlobalCustomError(
						status.value(),
						e.getMessage(),
						request.getRequestURI()
		);
		return ResponseEntity.status(status).body(error);
	}


}