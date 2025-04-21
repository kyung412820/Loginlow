package com.example.exception;

public class InvalidCredentialsException extends RuntimeException {
	public InvalidCredentialsException() {
		super("INVALID_CREDENTIALS: 아이디 또는 비밀번호가 올바르지 않습니다.");
	}
}