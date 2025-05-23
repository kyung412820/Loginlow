# 🛡️ Spring Boot JWT 인증 과제

## 📌 프로젝트 개요
- **회원가입·로그인** 기능 구현  
- **JWT** 기반 인증 / **역할 기반 인가**(Role‑based Authorization)  
- **Swagger**(OpenAPI)로 API 명세 제공  
- **메모리 저장소** 사용 → 별도 DB 없이 실행  

---

## ✅ 기능 요약

| 기능 | 설명 |
|------|------|
| **회원가입** | `/signup` — USER·ADMIN 모두 가능 |
| **로그인** | `/login` — JWT Access Token 발급 |
| **관리자 권한 부여** | `/admin/users/{id}/roles` — ADMIN만 호출 가능 |
| **JWT 인증** | `Authorization: Bearer <token>` 헤더로 보호 API 접근 |
| **역할 인가** | USER vs ADMIN 권한 분리 |
| **에러 응답** | 코드·메시지 통일 포맷 제공 |

---

## 🧪 주요 API 명세

### 🔐 회원가입 `POST /signup`
```json
// Request
{
  "username": "JIN HO",
  "password": "12341234",
  "nickname": "Mentos"
}

// Success Response
{
  "username": "JIN HO",
  "nickname": "Mentos",
  "roles": [ { "role": "USER" } ]
}

// Fail – 이미 존재
{
  "error": {
    "code": "USER_ALREADY_EXISTS",
    "message": "이미 가입된 사용자입니다."
  }
}
```

### 🔐 로그인 `POST /login`
```json
// Request
{
  "username": "JIN HO",
  "password": "12341234"
}

// Success
{ "token": "eyJhbGciOiJIUzI1NiIsInR..." }

// Fail – 자격 오류
{
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "아이디 또는 비밀번호가 올바르지 않습니다."
  }
}
```

### 🛡️ 관리자 권한 부여 `PATCH /admin/users/{userId}/roles`
*Headers* `Authorization: Bearer <accessToken>`
```json
// Success
{
  "username": "JIN HO",
  "nickname": "Mentos",
  "roles": [ { "role": "ADMIN" } ]
}

// Fail – 권한 없음
{
  "error": {
    "code": "ACCESS_DENIED",
    "message": "접근 권한이 없습니다."
  }
}
```

---

## 🧪 테스트 실행
```bash
# Gradle 프로젝트
./gradlew test
```
* 회원가입·로그인·권한 부여 정상/실패 케이스  
* JWT 유효성·권한 검증 시나리오 포함  

---

## 🧰 기술 스택
- Java 17 · Spring Boot 3.x · Spring Security  
- JWT (jjwt) · Swagger (springdoc‑openapi)  
- JUnit 5 · MockMvc  

---

## 🔎 Swagger 문서
**URL** `http://localhost:8080/swagger-ui/index.html`  
→ 모든 엔드포인트·파라미터·예시·에러 코드 확인 가능

---

## 🗄️ 데이터 저장소
- `Map` + `AtomicLong` 기반 **In‑Memory Repository**  
- 외부 DB·파일 시스템 사용 ❌

---

## ☁️ AWS EC2 배포 (미진행)
본 과제는 EC2 배포는 하지 못했습니다..

---

## ⚠️ 공통 에러 응답 포맷
```json
{
  "error": {
    "code": "ERROR_CODE",
    "message": "에러 설명 메시지"
  }
}
```
예) `INVALID_TOKEN` · `USER_ALREADY_EXISTS` · `ACCESS_DENIED` · `INVALID_CREDENTIALS`

---

## 📂 프로젝트 구조
```
com.example
├── controller   # REST API
├── dto          # 요청/응답 DTO
├── model        # User, Role
├── repository   # 메모리 저장소
├── security     # JWT 유틸·필터
├── service      # 비즈니스 로직
└── exception    # 커스텀 예외·핸들러
```
