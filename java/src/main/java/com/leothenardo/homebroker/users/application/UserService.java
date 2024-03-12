package com.leothenardo.homebroker.users.application;

import com.leothenardo.homebroker.users.dtos.UserDTO;
import com.leothenardo.homebroker.users.entities.User;
import com.leothenardo.homebroker.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthService authService;

	public Optional<UserDTO> getByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			return Optional.empty();
		}
		var userResponse = UserDTO.from(user);
		return Optional.of(userResponse);
	}

	public Optional<UserDTO> getByProviderId(String providerId) {
		User user = userRepository.findByProviderId(providerId);
		if (user == null) {
			return Optional.empty();
		}
		var userResponse = UserDTO.from(user);
		return Optional.of(userResponse);
	}

	@Transactional
	public void updateProfilePicture(String url) {
		UserDTO userDTO = authService.getMe();
		User user = userRepository.findByUsername(userDTO.getUsername());
		user.setPicture(url);
		userRepository.save(user);
	}

	public Optional<UserDTO> getById(String id) {
		User user = userRepository.findById(id).orElse(null);
		if (user == null) {
			return Optional.empty();
		}
		return Optional.of(UserDTO.from(user));
	}
}
