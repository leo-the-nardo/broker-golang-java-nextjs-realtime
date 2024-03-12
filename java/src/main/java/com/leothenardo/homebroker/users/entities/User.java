package com.leothenardo.homebroker.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leothenardo.homebroker._common.exceptions.EntityValidationException;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(User.COLLECTION_NAME)
public class User {
	public static final String COLLECTION_NAME = "users";
	public static final UserRole DEFAULT_ROLE = UserRole.ROLE_USER;

	@Id
	@Field(targetType = FieldType.STRING)
	private String id;

	@Field(name = "username")
	private String username;

	@JsonIgnore
	@Field(name = "password")
	private String password;

	@NotBlank
	@Field(name = "name")
	private String name;

	@Field(name = "picture")
	private String picture;

	@Field(name = "email_verified")
	private Instant emailVerified;

	@Field(name = "provider")
	private Provider provider;

	@Field(name = "provider_id") //user_id on provider
	private String providerId;

	@Field(name = "two_factor_enabled")
	private Instant twoFactorEnabled;

	@Field(name = "wallet_id")
	private String walletId;

	@Field(name = "roles")
	private Set<UserRole> roles;

	@Field(name = "two_factor_confirmations")
	private Set<TwoFactorConfirmation> twoFactorConfirmations;

	public static User createCredential(String username, String password, String name, String walletId) {
		var defaultRoles = new HashSet<UserRole>();
		defaultRoles.add(DEFAULT_ROLE);
		var user = new User(
						UUID.randomUUID().toString(),
						username,
						password,
						name,
						null,
						null,
						Provider.CREDENTIAL,
						null,
						Instant.now(),
						walletId,
						defaultRoles,
						new HashSet<>());
		user.validate();
		return user;
	}

	public static User createProvider(String username, String name, String picture, Provider provider, String providerId, String walletId) {
		var defaultRoles = new HashSet<UserRole>();
		defaultRoles.add(DEFAULT_ROLE);
		var user = new User(
						UUID.randomUUID().toString(),
						username,
						null,
						name,
						picture,
						Instant.now(),
						provider,
						providerId,
						Instant.now(),
						walletId,
						defaultRoles,
						new HashSet<>());
		user.validate();
		return user;
	}


	private void validate() {
		var errors = new ArrayList<String>();
		if (roles == null || roles.isEmpty()) {
			errors.add("At least one Role is required");
		}
		if (username == null || username.isBlank() || username.length() > 50 || username.length() < 3) {
			errors.add("Invalid username, actual: " + username);
		}

		if (name == null || name.isBlank() || name.length() > 255 || name.length() < 2) {
			errors.add("Invalid name, actual: " + name);
		}
		if (provider == null) {
			errors.add("Provider is required");
		}
		if (provider == Provider.CREDENTIAL) {
			if (password == null || password.isBlank() || password.length() > 255 || password.length() < 8) {
				errors.add("Invalid password, actual: " + password);
			}
		}
		if (provider != Provider.CREDENTIAL && provider != null) {
			if (providerId == null || providerId.isBlank() || providerId.length() > 255 || providerId.length() < 2) {
				errors.add("ProviderId is required");
			}
		}
		if (!errors.isEmpty()) {
			throw new EntityValidationException(errors);
		}
	}
}
