package com.example.model;

import java.util.HashSet;
import java.util.Set;

public class User {
	private Long id;
	private String username;
	private String password;
	private String nickname;
	private Set<Role> roles = new HashSet<>();

	// getters & setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getNickname() { return nickname; }
	public void setNickname(String nickname) { this.nickname = nickname; }
	public Set<Role> getRoles() { return roles; }
}