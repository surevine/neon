package com.surevine.neon.badges.service;

public class BadgeValidationException extends RuntimeException {
	
	private static final long serialVersionUID = 1828748603462641349L;

	public BadgeValidationException(String message) {
		super(message);
	}
	
	public BadgeValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
