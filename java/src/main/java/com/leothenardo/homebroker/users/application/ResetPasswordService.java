package com.leothenardo.homebroker.users.application;

import com.leothenardo.homebroker._providers.EmailProvider;
import com.leothenardo.homebroker.users.dtos.UserDTO;
import com.leothenardo.homebroker.users.entities.ResetPasswordToken;
import com.leothenardo.homebroker.users.entities.User;
import com.leothenardo.homebroker.users.repositories.ResetPasswordTokenRepository;
import com.leothenardo.homebroker.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class ResetPasswordService {
	@Autowired
	private ResetPasswordTokenRepository resetPasswordTokenRepository;
	@Autowired
	private EmailProvider emailService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;

	@Value("${email.password.expires-minutes}")
	private int EXPIRES_MINUTES;

	@Value("${email.password.url}")
	private String URL;

	public void sendResetPasswordEmail(String email) {
		System.out.println("Sending reset password email to " + email);
		UserDTO user = userService.getByUsername(email).orElse(null);
		if (user == null) {
			System.out.println("Failed to send: User not found");
			return;
		}
		Optional<ResetPasswordToken> token = resetPasswordTokenRepository.findByUserId(user.getId());
		if (token.isPresent()) {
			if (!token.get().isExpired()) {
				System.out.println("Failed to send: current Token not expired yet");
				return;
			}
			resetPasswordTokenRepository.delete(token.get());
		}
		var expires = Instant.now().plusSeconds(EXPIRES_MINUTES * 60L);
		ResetPasswordToken newToken = ResetPasswordToken.create(expires, user.getId());
		String link = URL + "?token=" + newToken.getToken();
		emailService.sendResetPasswordEmail(user.getUsername(), link, user.getName());
		resetPasswordTokenRepository.save(newToken);
	}


	public boolean resetPassword(String comingToken, String password) {
		if (comingToken == null || password == null || password.isEmpty() || comingToken.isEmpty() || password.length() < 6) {
			System.out.println("Failed to reset: unexpected token or password");
			return false;
		}
		ResetPasswordToken token = resetPasswordTokenRepository.findByToken(comingToken).orElse(null);
		if (token == null) {
			System.out.println("Failed to reset: Token not found");
			return false;
		}
		if (token.isExpired()) {
			System.out.println("Failed to reset: Token expired");
			return false;
		}
		resetPasswordTokenRepository.delete(token);
		User user = userRepository.findById(token.getUserId()).orElseThrow();
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);

		System.out.println("Successfully Reset pass to: " + user.getUsername());
		return true;
	}
}
