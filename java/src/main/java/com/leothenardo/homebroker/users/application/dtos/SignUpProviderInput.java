package com.leothenardo.homebroker.users.application.dtos;

import com.leothenardo.homebroker.users.entities.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpProviderInput {
	private String username;
	private String name;
	private String picture;
	private Provider provider;
	private String providerId;
}
