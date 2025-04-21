package com.example;

import com.example.controller.AdminController;
import com.example.security.JwtUtil;
import com.example.security.JwtAuthenticationFilter;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)  // 보안 필터 비활성화
public class AdminControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	// JwtUtil 과 JwtAuthenticationFilter를 MockBean으로 등록
	@MockBean
	private JwtAuthenticationFilter jwtFilter;

	@MockBean
	private JwtUtil jwtUtil;

	@Test
	void grantAdminNotFound() throws Exception {
		given(userService.grantAdmin(1L))
			.willThrow(new com.example.exception.UserNotFoundException());

		mockMvc.perform(patch("/admin/users/1/roles")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.error.code").value("USER_NOT_FOUND"));
	}
}
