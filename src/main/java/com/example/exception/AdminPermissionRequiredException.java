// AdminPermissionRequiredException.java
package com.example.exception;

public class AdminPermissionRequiredException extends RuntimeException {
	public AdminPermissionRequiredException() {
		super("ACCESS_DENIED: 관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.");
	}
}
