package com.leothenardo.homebroker.users.controllers;

import com.leothenardo.homebroker.users.application.AuthService;
import com.leothenardo.homebroker.users.application.ResetPasswordService;
import com.leothenardo.homebroker.users.dtos.ResetPasswordRequest;
import com.leothenardo.homebroker.users.dtos.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private ResetPasswordService resetPasswordService;

	@Autowired
	private AuthService authService;

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/profile")
	public ResponseEntity<UserDTO> getUserProfile() {
		log.info("Get /profile");
		UserDTO userDTO = authService.getMe().toResponse();
		return ResponseEntity.ok().body(userDTO);
	}

	@Operation(
					responses = {
									@ApiResponse(responseCode = "200", description = "OK"),
									@ApiResponse(responseCode = "410", description = "Gone")
					}
	)
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(
					@RequestBody ResetPasswordRequest resetPasswordRequest,
					@RequestParam(required = false) String token
	) {
		log.info("Post /reset-password");
		if (token == null) {
			resetPasswordService.sendResetPasswordEmail(resetPasswordRequest.getEmail());
			return ResponseEntity.ok().build();
		}
		var ok = resetPasswordService.resetPassword(token, resetPasswordRequest.getPassword());
		if (!ok) {
			return ResponseEntity.status(HttpStatus.GONE).build();
		}
		return ResponseEntity.ok().build();
	}


}
