package com.leothenardo.homebroker.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLoginResponse {
	private String accessToken;
	private String refreshToken;
	private boolean firstTimeLogin;
}
