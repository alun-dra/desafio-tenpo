package com.example.backend_tenpo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc  // 🔥 Habilitar WebMvc para asegurarse de que las configuraciones se aplican
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 🔥 Permitir CORS en todas las rutas
                        .allowedOrigins("http://localhost:5173", "http://localhost:5173/dashboard/home", "http://localhost:5173/dashboard/transactions")  // 🔥 Permitir peticiones desde el frontend
                        .allowedMethods("*")  // 🔥 Permitir todos los métodos HTTP
                        .allowedHeaders("*")  // 🔥 Permitir todos los headers
                        .allowCredentials(true);  // 🔥 Permitir cookies y autenticación con tokens
            }
        };
    }
}
