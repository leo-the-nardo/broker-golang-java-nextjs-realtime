package com.leothenardo.homebroker._providers;

public interface EmailProvider {
	void send(String to, String as, String title, String content);

	void sendConfirmationEmail(String to, String confirmationUrl, String name);

	void sendResetPasswordEmail(String to, String resetUrl, String name);
}
