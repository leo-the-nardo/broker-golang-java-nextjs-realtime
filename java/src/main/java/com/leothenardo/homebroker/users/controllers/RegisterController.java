package com.leothenardo.homebroker.users.controllers;

import com.leothenardo.homebroker.users.application.RegisterService;
import com.leothenardo.homebroker.users.dtos.SignupRequest;
import com.leothenardo.homebroker.users.dtos.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup")
public class RegisterController {
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	@Autowired
	private RegisterService registerService;

	@Operation(
					responses = {
									@ApiResponse(responseCode = "200", description = "OK"),
									@ApiResponse(responseCode = "409", description = "CONFLICT")
					}
	)
	@PostMapping()
	public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		log.info("Post /signup");

		UserDTO userDTO = registerService.signUpCredentials(signUpRequest);
		return ResponseEntity.ok(userDTO.toResponse());

	}

	@Operation(
					responses = {
									@ApiResponse(responseCode = "200", description = "OK"),
									@ApiResponse(responseCode = "410", description = "Gone")
					}
	)
	@GetMapping("/confirm")
	public ResponseEntity<?> confirm(@RequestParam("token") String token) {
		log.info("Get /signup/confirm?" + token);

		boolean ok = registerService.confirmToken(token);
		if (ok) {
			return ResponseEntity.ok().build();
		} else {
			// 410 Gone
			return ResponseEntity.status(HttpStatus.GONE).build();
		}
	}


}
