package com.leothenardo.homebroker.users.dtos;

import com.leothenardo.homebroker.users.entities.Provider;
import com.leothenardo.homebroker.users.entities.TwoFactorConfirmation;
import com.leothenardo.homebroker.users.entities.User;
import com.leothenardo.homebroker.users.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
	private String id;
	private String username;
	private String name;
	private Set<UserRole> roles;
	private Instant emailVerified;
	private Provider provider;
	private String picture;
	private Instant twoFactorEnabledAt;
	private Set<TwoFactorConfirmation> twoFactorConfirmations;
	private String walletId;

	public static UserDTO from(User user) {
		return new UserDTO(
						user.getId(),
						user.getUsername(),
						user.getName(),
						user.getRoles(),
						user.getEmailVerified(),
						user.getProvider(),
						user.getPicture(),
						user.getTwoFactorEnabled(),
						user.getTwoFactorConfirmations(),
						user.getWalletId()
		);
	}

	public UserDTO toResponse() {
		this.twoFactorConfirmations = null;
		this.walletId = null;
		return this;
	}
}
