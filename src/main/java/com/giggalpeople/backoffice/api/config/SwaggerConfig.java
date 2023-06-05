package com.giggalpeople.backoffice.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * <h2><b>Swagger 사용을 위한 Config Class</b></h2>
 */

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
			.version("v1.1.0")
			.title("기깔나는 사람들 Back Office")
			.description("API 명세서");

		return new OpenAPI().info(info);
	}
}
