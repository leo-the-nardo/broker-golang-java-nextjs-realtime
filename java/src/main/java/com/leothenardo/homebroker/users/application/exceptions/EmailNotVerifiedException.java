package com.leothenardo.homebroker.users.application.exceptions;

public class EmailNotVerifiedException extends RuntimeException {
	public EmailNotVerifiedException() {
		super("Email not verified");
	}
}
