package com.leothenardo.homebroker.users.application.dtos;

import lombok.Data;

@Data
public class GithubMailResponseDTO {
	private String email;
	private boolean verified;
	private boolean primary;
	private String visibility;
}
