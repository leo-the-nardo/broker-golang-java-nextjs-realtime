package com.leothenardo.homebroker.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDTO {

	private String accessToken;
	private String refreshToken;
	private Long expiresIn;
}
