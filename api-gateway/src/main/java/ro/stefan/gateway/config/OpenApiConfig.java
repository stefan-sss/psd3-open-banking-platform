package ro.stefan.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
	@Bean
	public OpenAPI getewayOpenAPI() {
		return new OpenAPI()
			.info(new Info().title("PSD3 API Gateway"))
			.externalDocs(
					new ExternalDocumentation()
					.description("Project documentation")
					.url("https://github.com/stefan-sss/psd3-open-banking-platform/blob/main/README.md"));
		
	}
	
}
