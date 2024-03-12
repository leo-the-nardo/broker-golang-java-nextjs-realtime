package com.leothenardo.homebroker.users.application;

import com.leothenardo.homebroker._providers.EmailService;
import com.leothenardo.homebroker.users.entities.VerificationToken;
import com.leothenardo.homebroker.users.repositories.UserRepository;
import com.leothenardo.homebroker.users.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Component
public class VerificationTokenService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	@Autowired
	private EmailService emailService;

	@Value("${email.verification.url}")
	private String URL;

	@Value("${email.verification.expires-minutes}")
	private long EXPIRES_MINUTES;

	@Transactional
	public boolean confirm(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElse(null);
		if (verificationToken == null) {
			return false;
		}
		if (verificationToken.isExpired()) { // token expired
			verificationTokenRepository.delete(verificationToken);
			send(verificationToken.getUsername());
			return false;
		}
		var user = userRepository.findByUsername(verificationToken.getUsername());
		user.setEmailVerified(Instant.now());
		userRepository.save(user);
		verificationTokenRepository.delete(verificationToken);
		return true;
	}


	public void resend(String email) {
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByUsername(email);
		if (verificationToken.isEmpty()) {
			System.out.println("is empty");
			justSend(email);
			return;
		}
		boolean isExpired = verificationToken.get().isExpired();
		if (!isExpired) {
			System.out.println("not expired");
			System.out.println(verificationToken.get().getExpiryDate());
			System.out.println(Instant.now());
			return;
		}
		System.out.println("resend expired");
		verificationTokenRepository.delete(verificationToken.get());
		justSend(email);
	}

	@Transactional
	public void send(String email) {
		Optional<VerificationToken> oldToken = verificationTokenRepository.findByUsername(email);
		oldToken.ifPresent(token -> verificationTokenRepository.delete(token));
		justSend(email);
	}


	@Async
	protected void justSend(String email) {
		System.out.println("justSend");
		var expires = Instant.now().plusSeconds(EXPIRES_MINUTES * 60);
		VerificationToken verificationToken = VerificationToken.create(expires, email);
		String link = URL + "?token=" + verificationToken.getToken();
		System.out.println(link);
		var user = userRepository.findByUsername(email);
		verificationTokenRepository.save(verificationToken);
		emailService.sendConfirmationEmail(email, link, user.getName());
	}

}
