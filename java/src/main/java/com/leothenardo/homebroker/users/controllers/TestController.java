package com.leothenardo.homebroker.users.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/testAdmin")
	public String test() {
		try {
			return "Welcome big admin";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/testUser")
	public String testUser() {
		try {
			return "Welcome sir user";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
