package com.leothenardo.homebroker.users.application;

import com.leothenardo.homebroker.users.application.exceptions.EmailNotVerifiedException;
import com.leothenardo.homebroker.users.application.exceptions.TwoFactorConfirmationRequiredException;
import com.leothenardo.homebroker.users.dtos.JwtResponseDTO;
import com.leothenardo.homebroker.users.dtos.SignInRequest;
import com.leothenardo.homebroker.users.dtos.UserDTO;
import com.leothenardo.homebroker.users.helpers.CustomUserDetails;
import com.leothenardo.homebroker.users.entities.Provider;
import com.leothenardo.homebroker.users.entities.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CredentialService {
	@Autowired
	private VerificationTokenService verificationTokenService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private RefreshTokenService refreshTokenService;
	@Autowired
	private TwoFactorService twoFactorService;
	@Autowired
	private AuthenticationManager authenticationManager;

	public JwtResponseDTO login(SignInRequest signIn) {
		var user = authenticate(signIn.getUsername(), signIn.getPassword());
		if (user.getProvider() != null && !user.getProvider().equals(Provider.CREDENTIAL)) { //unexpected if
			throw new UsernameNotFoundException("invalid user request..!!");
		}
		if (user.getEmailVerified() == null) {
			verificationTokenService.resend(user.getUsername());
			throw new EmailNotVerifiedException();
		}
		if (user.getTwoFactorEnabledAt() != null) {
			var comingTwoFactorToken = signIn.getToken();
			if (comingTwoFactorToken == null) {
				System.out.println("Two factor token not found. Sending email");
				twoFactorService.sendEmail(user.getUsername());
				throw new TwoFactorConfirmationRequiredException();
			}
			if (!verify(user, comingTwoFactorToken)) {
				System.out.println("Two factor token not valid");
				twoFactorService.sendEmail(user.getUsername());
				throw new TwoFactorConfirmationRequiredException();
			}
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

	public boolean verify(UserDTO user, String comingTwoFactorCookie) {
		return user.getTwoFactorConfirmations().stream().anyMatch(confirmation -> confirmation.getCookie().equals(comingTwoFactorCookie));
	}


	private UserDTO authenticate(String username, String password) {
		Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(username, password)
		);
		if (!authentication.isAuthenticated()) {
			throw new UsernameNotFoundException("invalid user request..!!");
		}

		CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();
		var user = UserDTO.from(userDetail.getUser());
		return user;
	}

}

