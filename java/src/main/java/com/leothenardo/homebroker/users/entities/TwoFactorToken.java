package com.leothenardo.homebroker.users.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(TwoFactorToken.COLLECTION_NAME)
public class TwoFactorToken {
	public static final String COLLECTION_NAME = "two_factor_tokens";
	@Id
	@Field(name = "_id")
	private String token;

	@Field("expiry_date")
	private Instant expiryDate;

	@Field("username")
	private String username;

	public static TwoFactorToken create(Instant expiryDate, String username) {
		// Generate a random six-digit number
		int min = 100000;
		int max = 1000000;
		int token = ThreadLocalRandom.current().nextInt(min, max);
		return new TwoFactorToken(String.valueOf(token), expiryDate, username);
	}

	public boolean isExpired() {
		return Instant.now().isAfter(expiryDate);
	}
}
