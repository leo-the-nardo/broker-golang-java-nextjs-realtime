package com.leothenardo.homebroker.users.application;

import com.leothenardo.homebroker._common.exceptions.UnauthorizedException;
import com.leothenardo.homebroker.users.dtos.UserDTO;
import com.leothenardo.homebroker.users.helpers.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	public UserDTO getMe() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();
		return UserDTO.from(userDetail.getUser());
	}

	public void isMe(String id) {
		var isMe = getMe().getId().equals(id);
		if (!isMe) {
			throw new UnauthorizedException();
		}
	}

}
