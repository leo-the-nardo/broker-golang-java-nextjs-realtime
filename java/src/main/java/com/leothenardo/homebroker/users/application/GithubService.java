package com.leothenardo.homebroker.users.application;

import com.leothenardo.homebroker._common.exceptions.AlreadyExistsException;
import com.leothenardo.homebroker._common.exceptions.UnauthorizedException;
import com.leothenardo.homebroker.users.application.dtos.GithubMailResponseDTO;
import com.leothenardo.homebroker.users.application.dtos.GithubResponseDTO;
import com.leothenardo.homebroker.users.application.dtos.SignUpProviderInput;
import com.leothenardo.homebroker.users.dtos.JwtResponseDTO;
import com.leothenardo.homebroker.users.entities.Provider;
import com.leothenardo.homebroker.users.entities.RefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;


@Component
public class GithubService {
	private final static Logger log = LoggerFactory.getLogger(GithubService.class);
	private final static String GITHUB_API_URL = "https://api.github.com/user";
	private final WebClient webClient = WebClient.create();
	@Autowired
	private UserService userService;
	@Autowired
	private RegisterService registerService;
	@Autowired
	private RefreshTokenService refreshTokenService;
	@Autowired
	private JwtService jwtService;

	@Transactional
	public JwtResponseDTO signIn(String token) {
		var payload = parseToken(token).orElseThrow(UnauthorizedException::new);
		var providerId = String.valueOf(payload.getId());
		var email = payload.getEmail();
		var user = userService.getByProviderId(providerId).orElse(null);
		if (user == null) {
			userService.getByUsername(email).ifPresent(u -> {
				throw new AlreadyExistsException(email);
			});
			user = registerService.signUpProvider(new SignUpProviderInput(
							email,
							payload.getName(),
							payload.getAvatarUrl(),
							Provider.GITHUB,
							providerId
			));
		}
		RefreshToken refreshToken = refreshTokenService.create(user.getId());
		String accessToken = jwtService.generateToken(user.getId());
		var expiresIn = jwtService.extractExpiration(accessToken).getTime() / 1000;
		return JwtResponseDTO.builder()
						.accessToken(jwtService.generateToken(user.getId()))
						.refreshToken(refreshToken.getToken())
						.expiresIn(expiresIn)
						.build();
	}

	public Optional<GithubResponseDTO> parseToken(String token) {
		try {
			GithubResponseDTO response = webClient.get()
							.uri(GITHUB_API_URL)
							.headers(httpHeaders -> httpHeaders.setBearerAuth(token))
							.retrieve()
							.bodyToMono(GithubResponseDTO.class)
							.block();
			List<GithubMailResponseDTO> responseMail = webClient.get()
							.uri(GITHUB_API_URL + "/emails")
							.headers(httpHeaders -> httpHeaders.setBearerAuth(token))
							.retrieve()
							.bodyToMono(new ParameterizedTypeReference<List<GithubMailResponseDTO>>() {
							})
							.block();
			response.setEmail(responseMail.get(0).getEmail());
			log.info("Github response: {}", response);
			return Optional.of(response);
		} catch (WebClientResponseException e) {
			log.error("Can't parse token", e);
			return Optional.empty();
		}
	}

}
