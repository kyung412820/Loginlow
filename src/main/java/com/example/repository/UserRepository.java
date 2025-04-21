package com.example.repository;

import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

	// 메모리 내 저장소
	private final Map<Long, User> usersById = new HashMap<>();
	private final Map<String, User> usersByUsername = new HashMap<>();
	private final AtomicLong idSequence = new AtomicLong(1);

	// 저장
	public User save(User user) {
		if (user.getId() == null) {
			user.setId(idSequence.getAndIncrement());
		}
		usersById.put(user.getId(), user);
		usersByUsername.put(user.getUsername(), user);
		return user;
	}

	// username으로 조회
	public Optional<User> findByUsername(String username) {
		return Optional.ofNullable(usersByUsername.get(username));
	}

	// id로 조회
	public Optional<User> findById(Long id) {
		return Optional.ofNullable(usersById.get(id));
	}
}
