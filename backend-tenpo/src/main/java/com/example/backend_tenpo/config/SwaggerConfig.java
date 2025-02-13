package com.example.backend_tenpo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Transacciones")
                        .version("1.0")
                        .description("Documentación de la API para gestionar transacciones")
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth")) // ✅ Requerir token en todas las peticiones
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .name("Authorization") // ✅ Asegurar que el header se llame "Authorization"
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT") // ✅ Formato del token
                        )
                );
    }
}
