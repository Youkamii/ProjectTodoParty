package com.sparta.projecttodoparty.common;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
				.version("v1.0.0")
				.title("개인 주차 프로젝트");

		String jwt = "JWT";
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
		Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
				.name(jwt)
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT")
		);

		return new OpenAPI()
				.info(info)
				.addSecurityItem(securityRequirement)
				.components(components);
	}
}
