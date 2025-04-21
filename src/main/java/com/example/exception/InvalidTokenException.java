// InvalidTokenException.java
package com.example.exception;

public class InvalidTokenException extends RuntimeException {
	public InvalidTokenException() {
		super("INVALID_TOKEN: 유효하지 않은 인증 토큰입니다.");
	}
}
