package com.leothenardo.homebroker.users.application;

import com.leothenardo.homebroker._common.exceptions.UnauthorizedException;
import com.leothenardo.homebroker.users.dtos.JwtResponseDTO;
import com.leothenardo.homebroker.users.entities.RefreshToken;
import com.leothenardo.homebroker.users.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class RefreshTokenService {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	@Value("${refresh-token.expiration-days}")
	private long EXPIRATION_DAYS;

	@Transactional
	public RefreshToken create(String userId) {
		RefreshToken old = refreshTokenRepository.findByUserId(userId);
		if (old != null) {
			refreshTokenRepository.delete(old); //here it clean the old "session" of the user
		}
		var expires = Instant.now().plusSeconds(EXPIRATION_DAYS * 24 * 60 * 60);
		var refreshToken = RefreshToken.create(expires, userId);
		refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	@Transactional
	public JwtResponseDTO refresh(String refreshToken) {
		var oldToken = refreshTokenRepository.findByToken(refreshToken);
		if (oldToken == null) {
			throw new UnauthorizedException();
		}
		if (oldToken.isExpired()) {
			refreshTokenRepository.delete(oldToken);
			throw new UnauthorizedException();
		}
		String newAccessToken = jwtService.generateToken(oldToken.getUserId());
		var expiresIn = jwtService.extractExpiration(newAccessToken).getTime() / 1000;
		return JwtResponseDTO.builder()
						.accessToken(newAccessToken)
						.refreshToken(refreshToken)
						.expiresIn(expiresIn)
						.build();
	}
}
