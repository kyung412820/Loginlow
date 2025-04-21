package com.example.controller;

import com.example.dto.AuthRequest;
import com.example.dto.AuthResponse;
import com.example.dto.SignupRequest;
import com.example.model.User;
import com.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {
	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
		User user = userService.signup(req);
		Map<String, Object> body = Map.of(
			"username", user.getUsername(),
			"nickname", user.getNickname(),
			"roles", user.getRoles().stream()
				.map(r -> Map.of("role", r.name())).collect(Collectors.toList())
		);
		return ResponseEntity.ok(body);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
		AuthResponse resp = userService.login(req);
		return ResponseEntity.ok(resp);
	}
}