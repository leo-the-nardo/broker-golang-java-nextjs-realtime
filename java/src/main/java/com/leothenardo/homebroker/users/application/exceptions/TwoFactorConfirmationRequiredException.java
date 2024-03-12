package com.leothenardo.homebroker.users.application.exceptions;

public class TwoFactorConfirmationRequiredException extends RuntimeException {
	public TwoFactorConfirmationRequiredException() {
		super("Two factor confirmation required");
	}
}
