package com.example.exception;

import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<?> handleUserExists(UserAlreadyExistsException ex) {
		String[] parts = ex.getMessage().split(": ", 2);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(Map.of("error", Map.of("code", parts[0], "message", parts[1])));
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<?> handleInvalidCred(InvalidCredentialsException ex) {
		String[] parts = ex.getMessage().split(": ", 2);
		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(Map.of("error", Map.of("code", parts[0], "message", parts[1])));
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
		String[] parts = ex.getMessage().split(": ", 2);
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(Map.of("error", Map.of("code", parts[0], "message", parts[1])));
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<?> handleInvalidToken(InvalidTokenException ex) {
		String[] parts = ex.getMessage().split(": ", 2);
		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(Map.of("error", Map.of("code", parts[0], "message", parts[1])));
	}

	@ExceptionHandler(AdminPermissionRequiredException.class)
	public ResponseEntity<?> handleAdminPermission(AdminPermissionRequiredException ex) {
		String[] parts = ex.getMessage().split(": ", 2);
		return ResponseEntity
			.status(HttpStatus.FORBIDDEN)
			.body(Map.of("error", Map.of("code", parts[0], "message", parts[1])));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
		return ResponseEntity
			.status(HttpStatus.FORBIDDEN)
			.body(Map.of("error", Map.of("code", "ACCESS_DENIED", "message", "접근 권한이 없습니다.")));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleOther(Exception ex) {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(Map.of("error", Map.of("code", "INTERNAL_ERROR", "message", ex.getMessage())));
	}
}
