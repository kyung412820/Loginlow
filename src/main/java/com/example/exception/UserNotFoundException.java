package com.example.exception;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException() {
		super("USER_NOT_FOUND: 사용자를 찾을 수 없습니다.");
	}
}