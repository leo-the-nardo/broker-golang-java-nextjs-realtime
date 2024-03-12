package com.leothenardo.homebroker._common.exceptions;

public class UnauthorizedException extends RuntimeException {
	public UnauthorizedException() {
		super("Unauthorized");
	}
}
