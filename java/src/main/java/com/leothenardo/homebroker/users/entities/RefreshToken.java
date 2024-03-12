package com.leothenardo.homebroker.users.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(RefreshToken.COLLECTION_NAME)
public class RefreshToken {
	public static final String COLLECTION_NAME = "refresh_tokens";
	@Id
	@Field(name = "_id", targetType = FieldType.STRING)
	private String token;

	@Field("expiry_date")
	private Instant expiryDate;

	@Indexed
	@Field("user_id")
	private String userId;

	public static RefreshToken create(Instant expiryDate, String userId) {
		return new RefreshToken(UUID.randomUUID().toString(), expiryDate, userId);
	}

	public boolean isExpired() {
		return Instant.now().isAfter(expiryDate);
	}
}
