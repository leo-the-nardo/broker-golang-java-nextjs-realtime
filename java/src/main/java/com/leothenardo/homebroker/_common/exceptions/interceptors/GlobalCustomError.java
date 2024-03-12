package com.leothenardo.homebroker._common.exceptions.interceptors;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GlobalCustomError {
	private Instant timestamp = Instant.now();
	private Integer status;
	private String message;
	private String path;
	private List<String> errors = new ArrayList<>();

	public GlobalCustomError() {
	}

	public GlobalCustomError(Integer status, String message, String path) {
		this.status = status;
		this.message = message;
		this.path = path;
		errors.add(message);
	}

	public GlobalCustomError(Integer status, String message, String path, List<String> errors) {
		this.status = status;
		this.message = message;
		this.path = path;
		this.errors = errors;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getErrors() {
		return errors;
	}

	public Integer getStatus() {
		return status;
	}

	public String getPath() {
		return path;
	}
}