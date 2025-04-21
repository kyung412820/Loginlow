package com.example;

import com.example.dto.AuthRequest;
import com.example.dto.SignupRequest;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.InvalidCredentialsException;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;
import com.example.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
	private UserRepository repo;
	private PasswordEncoder encoder;
	private JwtUtil jwtUtil;
	private UserService service;

	@BeforeEach
	void init() {
		repo = mock(UserRepository.class);
		encoder = mock(PasswordEncoder.class);
		jwtUtil = mock(JwtUtil.class);

		// 로그인 성공 시 항상 이 토큰을 리턴하도록 설정
		when(jwtUtil.generateToken(any())).thenReturn("dummy-token");

		service = new UserService(repo, encoder, jwtUtil);
	}

	@Test
	void signupDuplicate() {
		SignupRequest req = new SignupRequest();
		req.setUsername("dup");
		when(repo.findByUsername("dup"))
			.thenReturn(java.util.Optional.of(new User()));
		assertThrows(UserAlreadyExistsException.class,
			() -> service.signup(req));
	}

	@Test
	void loginSuccess() {
		User user = new User();
		user.setUsername("u");
		user.setPassword("encoded-pass");
		when(repo.findByUsername("u"))
			.thenReturn(java.util.Optional.of(user));
		// 인코더도 목으로, 암호 비교를 true로 설정
		when(encoder.matches("p", "encoded-pass")).thenReturn(true);

		AuthRequest req = new AuthRequest();
		req.setUsername("u");
		req.setPassword("p");

		// 실제 토큰 대신 'dummy-token' 이 리턴되는지 확인
		assertEquals("dummy-token",
			service.login(req).getToken());
	}

	@Test
	void loginInvalid() {
		when(repo.findByUsername("u"))
			.thenReturn(java.util.Optional.empty());
		AuthRequest req = new AuthRequest();
		req.setUsername("u");
		req.setPassword("p");
		assertThrows(InvalidCredentialsException.class,
			() -> service.login(req));
	}
}
