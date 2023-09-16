package com.leothenardo.homebroker.common.exceptions;

import java.util.Arrays;
import java.util.List;

public class EntityValidationException extends RuntimeException {
	private final List<String> validationErrors;

	public EntityValidationException(List<String> validationErrors) {
		super("Entity validation failed");
		this.validationErrors = validationErrors;
	}

	public List<String> getValidationErrors() {
		return validationErrors;
	}

}