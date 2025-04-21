package com.example.exception;

public class UserAlreadyExistsException extends RuntimeException {
	public UserAlreadyExistsException() {
		super("USER_ALREADY_EXISTS: 이미 가입된 사용자입니다.");
	}
}