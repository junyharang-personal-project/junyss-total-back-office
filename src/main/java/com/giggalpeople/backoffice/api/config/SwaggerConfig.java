package com.giggalpeople.backoffice.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * <h2><b>Swagger 사용을 위한 Config Class</b></h2>
 */

@Configuration
@Profile("!prod")
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI(@Value("${springdoc.swagger-ui.version}") String springdocVersion) {
		Info info = new Info()
			.title("기깔나는 사람들 Back Office")
			.version(springdocVersion)
			.description("API 명세서");

		return new OpenAPI()
			.components(new Components())
			.info(info);
	}
}
