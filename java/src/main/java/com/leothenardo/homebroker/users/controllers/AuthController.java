package com.leothenardo.homebroker.users.controllers;


import com.leothenardo.homebroker.users.application.*;
import com.leothenardo.homebroker.users.application.exceptions.EmailNotVerifiedException;
import com.leothenardo.homebroker.users.application.exceptions.TwoFactorConfirmationRequiredException;
import com.leothenardo.homebroker.users.dtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private GoogleService googleService;
	@Autowired
	private GithubService githubService;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private TwoFactorService twoFactorService;
	@Autowired
	private RefreshTokenService refreshTokenService;


	@Operation(
					responses = {
									@ApiResponse(responseCode = "200", description = "OK"),
									@ApiResponse(responseCode = "422", description = "Email not verified"),
									@ApiResponse(responseCode = "412", description = "Two factor confirmation required"),
									@ApiResponse(responseCode = "401", description = "Unauthorized")
					}
	)
	@PostMapping("/signin")
	public ResponseEntity<JwtResponseDTO> authenticate(@RequestBody SignInRequest signInRequest, HttpServletResponse response) {
		log.info("Post /signin");
		try {
			var jwtResponseDTO = credentialService.login(signInRequest);
			return ResponseEntity.ok(jwtResponseDTO);

		} catch (EmailNotVerifiedException e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();

		} catch (TwoFactorConfirmationRequiredException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).build();
		}
	}

	@Operation(
					responses = {
									@ApiResponse(responseCode = "200", description = "OK"),
									@ApiResponse(responseCode = "401", description = "Unauthorized")
					}
	)
	@PostMapping("/signin/google")
	public ResponseEntity<JwtResponseDTO> authenticate(@RequestBody GoogleAuthRequest authRequestDTO) {
		log.info("Post /signin/google");
		JwtResponseDTO user = googleService.signIn(authRequestDTO.getIdToken());
		return ResponseEntity.ok(user);

	}

	@Operation(
					responses = {
									@ApiResponse(responseCode = "200", description = "OK"),
									@ApiResponse(responseCode = "401", description = "Unauthorized")
					}
	)
	@PostMapping("/signin/github")
	public ResponseEntity<JwtResponseDTO> authenticate(@RequestBody GithubAuthRequest authRequestDTO) {
		log.info("Post /signin/github");
		JwtResponseDTO response = githubService.signIn(authRequestDTO.getAccessToken());
		return ResponseEntity.ok(response);
	}

	@Operation(
					responses = {
									@ApiResponse(responseCode = "200", description = "OK"),
									@ApiResponse(responseCode = "401", description = "Unauthorized")
					}
	)
	@PostMapping("/refreshToken")
	public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest, HttpServletResponse response) {
		log.info("Post /refreshToken");
		JwtResponseDTO tokens = refreshTokenService.refresh(refreshTokenRequest.getRefreshToken());
		return tokens;
	}

	@Operation(
					responses = {
									@ApiResponse(responseCode = "200", description = "OK"),
									@ApiResponse(responseCode = "401", description = "Unauthorized")
					}
	)
	@PostMapping("/twoFactor")
	public ResponseEntity<TwoFactorConfirmationResponse> confirmTwoFactor(
					@RequestBody TwoFactorConfirmationRequest request
	) {
		log.info("Post /twoFactor/confirm");
		Optional<String> cookie = twoFactorService.confirm(request.getToken());
		if (cookie.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.ok(new TwoFactorConfirmationResponse(cookie.get()));
	}

}
