package com.leothenardo.homebroker.users.application;

import com.leothenardo.homebroker._providers.EmailService;
import com.leothenardo.homebroker.users.dtos.UserDTO;
import com.leothenardo.homebroker.users.entities.TwoFactorConfirmation;
import com.leothenardo.homebroker.users.entities.TwoFactorToken;
import com.leothenardo.homebroker.users.entities.User;
import com.leothenardo.homebroker.users.repositories.TwoFactorRepository;
import com.leothenardo.homebroker.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TwoFactorService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TwoFactorRepository twoFactorRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserService userService;

	@Value("${email.verification.expires-minutes}")
	private long EXPIRES_MINUTES;


	public void sendEmail(String email) {
		TwoFactorToken token = twoFactorRepository.findByUsername(email);
		if (token != null) {
			if (!token.isExpired()) {
				System.out.println("Failed to send: current 2FA Token not expired yet");
				return;
			}
			twoFactorRepository.delete(token);
		}
		System.out.println("Sending 2FA email to " + email);
		UserDTO user = userService.getByUsername(email).orElse(null);
		if (user == null) {
			System.out.println("Failed to send: User not found");
			return;
		}
		var expires = Instant.now().plusSeconds(EXPIRES_MINUTES * 60);
		TwoFactorToken newToken = TwoFactorToken.create(expires, user.getUsername());
		emailService.sendTwoFactorEmail(user.getUsername(), newToken.getToken(), user.getName());
		twoFactorRepository.save(newToken);
	}

	public Optional<String> confirm(String token) {
		System.out.println("Confirming 2FA with token: " + token);
		if (token == null || token.length() != 6) {
			System.out.println("Failed to reset: unexpected token or password");
			return Optional.empty();
		}
		TwoFactorToken twoFactorToken = twoFactorRepository.findByToken(token);
		if (twoFactorToken == null) {
			System.out.println("Failed to confirm 2FA: Token not found");
			return Optional.empty();
		}
		if (twoFactorToken.isExpired()) {
			System.out.println("Failed to confirm 2FA: Token expired. Sending new token to user email.");
			sendEmail(twoFactorToken.getUsername());
			return Optional.empty();
		}
		twoFactorRepository.delete(twoFactorToken);
		User user = userRepository.findByUsername(twoFactorToken.getUsername());
		TwoFactorConfirmation confirmation = TwoFactorConfirmation.create();
		user.getTwoFactorConfirmations().add(confirmation);
		userRepository.save(user);
		System.out.println("Successfully confirmed 2FA to: " + user.getUsername());
		return Optional.of(confirmation.getCookie());
	}

	public boolean verify(UserDTO user, String comingTwoFactorCookie) {
		return user.getTwoFactorConfirmations().stream().anyMatch(confirmation -> confirmation.getCookie().equals(comingTwoFactorCookie));
	}
}
