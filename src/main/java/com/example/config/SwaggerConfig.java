// Kotlin 사용 시, Java 코드로 작성해도 무방
package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("JWT Auth API")
				.version("1.0.0")
				.description("Spring Boot 3 + Kotlin + JWT 기반 인증 API 문서"));
	}
}
