package com.example.service;

import com.example.dto.AuthRequest;
import com.example.dto.AuthResponse;
import com.example.dto.SignupRequest;
import com.example.exception.InvalidCredentialsException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
	private final UserRepository repo;
	private final PasswordEncoder encoder;
	private final JwtUtil jwtUtil;

	public UserService(UserRepository repo, PasswordEncoder encoder, JwtUtil jwtUtil) {
		this.repo = repo;
		this.encoder = encoder;
		this.jwtUtil = jwtUtil;
	}

	public User signup(SignupRequest req) {
		repo.findByUsername(req.getUsername()).ifPresent(u -> { throw new UserAlreadyExistsException(); });
		User user = new User();
		user.setUsername(req.getUsername());
		user.setPassword(encoder.encode(req.getPassword()));
		user.setNickname(req.getNickname());
		user.getRoles().add(Role.USER);
		return repo.save(user);
	}

	public AuthResponse login(AuthRequest req) {
		User user = repo.findByUsername(req.getUsername())
			.orElseThrow(InvalidCredentialsException::new);
		if (!encoder.matches(req.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException();
		}
		String token = jwtUtil.generateToken(loadUserByUsername(user.getUsername()));
		return new AuthResponse(token);
	}

	public User grantAdmin(Long userId) {
		User user = repo.findById(userId).orElseThrow(UserNotFoundException::new);
		user.getRoles().add(Role.ADMIN);
		return repo.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repo.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return org.springframework.security.core.userdetails.User
			.withUsername(user.getUsername())
			.password(user.getPassword())
			.roles(user.getRoles().stream()
				.map(Enum::name).toArray(String[]::new))
			.build();
	}
}