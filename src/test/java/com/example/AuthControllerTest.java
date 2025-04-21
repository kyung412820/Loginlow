package com.example;

import com.example.controller.AuthController;
import com.example.dto.AuthRequest;
import com.example.dto.SignupRequest;
import com.example.exception.InvalidCredentialsException;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.JwtAuthenticationFilter;
import com.example.security.JwtUtil;
import com.example.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)  // 필터 끄고 싶은 경우
public class AuthControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Mock
	private UserRepository repo;

	@Mock
	private PasswordEncoder encoder;

	// (필터나 util이 필요하면 @MockBean 추가)
	@MockBean
	private JwtUtil jwtUtil;
	@MockBean
	private JwtAuthenticationFilter jwtFilter;

	@InjectMocks
	private UserService service;

	@Test
	void signupSuccess() throws Exception {
		// 1) User 목 생성
		User u = mock(User.class);
		when(u.getUsername()).thenReturn("user");
		when(u.getNickname()).thenReturn("nick");
		when(u.getRoles()).thenReturn(Set.of(Role.USER));

		// 2) service.signup(any()) 호출 시 위 User 리턴
		given(userService.signup(any(SignupRequest.class)))
			.willReturn(u);

		// 3) 실제 요청/검증
		String json = """
        {"username":"user","password":"pass","nickname":"nick"}
        """;
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value("user"))
			.andExpect(jsonPath("$.nickname").value("nick"))
			.andExpect(jsonPath("$.roles[0].role").value("USER"));
	}

	@Test
	void loginWrongPassword() {
		User user = new User();
		user.setUsername("u");
		user.setPassword("enc");
		when(repo.findByUsername("u")).thenReturn(Optional.of(user));
		when(encoder.matches("wrong", "enc")).thenReturn(false);

		AuthRequest req = new AuthRequest();
		req.setUsername("u");
		req.setPassword("wrong");
		assertThrows(InvalidCredentialsException.class, () -> service.login(req));
	}

	@Test
	void loginInvalidCredentials() throws Exception {
		given(userService.login(any(AuthRequest.class)))
			.willThrow(new InvalidCredentialsException());

		String json = """
            {"username":"user","password":"wrong"}
            """;
		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"));
	}
}
