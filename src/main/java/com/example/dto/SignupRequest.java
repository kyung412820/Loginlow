package com.example.dto;

public class SignupRequest {
	private String username;
	private String password;
	private String nickname;
	// getters & setters
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getPassword() { return password; }
	public String getNickname() { return nickname; }
}