package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {
	private final UserService userService;

	public AdminController(UserService userService) {
		this.userService = userService;
	}

	@PatchMapping("/users/{userId}/roles")
	public ResponseEntity<?> grantAdmin(@PathVariable Long userId) {
		User user = userService.grantAdmin(userId);
		Map<String, Object> body = Map.of(
			"username", user.getUsername(),
			"nickname", user.getNickname(),
			"roles", user.getRoles().stream()
				.map(r -> Map.of("role", r.name())).collect(Collectors.toList())
		);
		return ResponseEntity.ok(body);
	}
}