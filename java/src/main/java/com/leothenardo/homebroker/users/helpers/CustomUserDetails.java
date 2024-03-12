package com.leothenardo.homebroker.users.helpers;


import com.leothenardo.homebroker.users.entities.User;
import com.leothenardo.homebroker.users.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// spring sec boilerplate
public class CustomUserDetails implements UserDetails {

	private final User user;
	private Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails(User byUser) {
		this.user = byUser;
		List<GrantedAuthority> auths = new ArrayList<>();

		for (UserRole role : byUser.getRoles()) {
			auths.add(new SimpleGrantedAuthority(role.name()));
		}
		this.authorities = auths;
	}

	@Override
	public String toString() {
		return "CustomUserDetails{" +
						"user=" + user +
						", authorities=" + authorities +
						'}';
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public User getUser() {
		return user;
	}
}
