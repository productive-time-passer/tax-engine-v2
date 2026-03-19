package com.taxengine.facts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taxEngineOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tax Engine v2 API")
                        .description("APIs for document ingestion, fact extraction, tax context, advisory output, and copilot chat.")
                        .version("v1"));
    }
}
