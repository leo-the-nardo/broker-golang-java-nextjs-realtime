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
@Document(ResetPasswordToken.COLLECTION_NAME)
public class ResetPasswordToken {
	public static final String COLLECTION_NAME = "reset_password_tokens";
	@Id
	@Field(name = "_id", targetType = FieldType.STRING)
	private String token;

	@Field("expiry_date")
	private Instant expiryDate;

	@Field("user_id")
	private String userId;

	public static ResetPasswordToken create(Instant expiryDate, String userId) {
		return new ResetPasswordToken(UUID.randomUUID().toString(), expiryDate, userId);
	}

	public boolean isExpired() {
		return Instant.now().isAfter(expiryDate);
	}
}
