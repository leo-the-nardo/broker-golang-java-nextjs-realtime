package com.leothenardo.homebroker.users.application;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.leothenardo.homebroker._common.exceptions.AlreadyExistsException;
import com.leothenardo.homebroker._common.exceptions.UnauthorizedException;
import com.leothenardo.homebroker.users.application.dtos.SignUpProviderInput;
import com.leothenardo.homebroker.users.dtos.JwtResponseDTO;
import com.leothenardo.homebroker.users.entities.Provider;
import com.leothenardo.homebroker.users.entities.RefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Component
public class GoogleService {
	private final static Logger log = LoggerFactory.getLogger(GoogleService.class);
	@Autowired
	private UserService userService;
	@Autowired
	private RegisterService registerService;
	@Autowired
	private RefreshTokenService refreshTokenService;
	@Autowired
	private JwtService jwtService;

	@Value("${google.client.id}")
	private String clientId;


	@Transactional
	public JwtResponseDTO signIn(String idToken) {
		var payload = parseToken(idToken).orElseThrow(UnauthorizedException::new);
		String email = payload.getEmail();
		String providerId = payload.getSubject();
		var user = userService.getByUsername(email).orElse(null);
		if (user == null) {
			String name = (String) payload.get("name");
			String picture = (String) payload.get("picture");
			user = registerService.signUpProvider(new SignUpProviderInput(
							email,
							name,
							picture,
							Provider.GOOGLE,
							providerId

			));
		}
		if (user.getProvider() != Provider.GOOGLE) {
			throw new AlreadyExistsException(email);
		}
		RefreshToken refreshToken = refreshTokenService.create(user.getId());
		String accessToken = jwtService.generateToken(user.getId());
		var expiresIn = jwtService.extractExpiration(accessToken).getTime() / 1000;
		return JwtResponseDTO.builder()
						.accessToken(accessToken)
						.refreshToken(refreshToken.getToken())
						.expiresIn(expiresIn)
						.build();
	}

	public Optional<GoogleIdToken.Payload> parseToken(String idToken) {
		JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
		NetHttpTransport transport = new NetHttpTransport();
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
						.setAudience(Collections.singletonList(clientId))
						.build();
		try {
			GoogleIdToken googleIdToken = verifier.verify(idToken);
			if (googleIdToken != null) {
				return Optional.of(googleIdToken.getPayload());
			} else {
				return Optional.empty();
			}
		} catch (Exception e) {
			log.error("Error while verifying google token", e);
			return Optional.empty();
		}
	}
}
