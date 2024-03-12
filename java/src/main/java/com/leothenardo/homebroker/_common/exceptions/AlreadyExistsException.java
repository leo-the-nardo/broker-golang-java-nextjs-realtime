package com.leothenardo.homebroker._common.exceptions;

public class AlreadyExistsException extends RuntimeException {
	public AlreadyExistsException(Object id) {
		super(id + " Already exists");
	}
}