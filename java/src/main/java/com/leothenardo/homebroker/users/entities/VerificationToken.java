package com.leothenardo.homebroker.users.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(VerificationToken.COLLECTION_NAME)
public class VerificationToken {
	public static final String COLLECTION_NAME = "verification_tokens";

	@Id
	@Field(name = "_id", targetType = FieldType.STRING)
	private String token;

	@Field("expiry_date")
	private Instant expiryDate;

	@Field("username")
	private String username;

	public static VerificationToken create(Instant expiryDate, String username) {
		return new VerificationToken(UUID.randomUUID().toString(), expiryDate, username);
	}

	public boolean isExpired() {
		return Instant.now().isAfter(expiryDate);
	}
}
