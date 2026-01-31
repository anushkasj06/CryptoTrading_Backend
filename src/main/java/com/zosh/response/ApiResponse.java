package com.zosh.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
	private String message;
	private boolean status;

	// MANUAL CONSTRUCTOR - REQUIRED
	// This fixes the "Cannot resolve constructor" error in your controller/service
	public ApiResponse(String message) {
		this.message = message;
		this.status = true; // Default status to true
	}
}