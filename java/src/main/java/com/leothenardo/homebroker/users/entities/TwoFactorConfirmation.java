package com.leothenardo.homebroker.users.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorConfirmation {
	@Field(name = "cookie")
	private String cookie;

	@Field(name = "confirmed_at")
	private Instant confirmedAt;

	public static TwoFactorConfirmation create() {
		return new TwoFactorConfirmation(UUID.randomUUID().toString(), Instant.now());
	}
}
