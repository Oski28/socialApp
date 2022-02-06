package com.example.socialApp.security.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CustomCorsFilter implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedOrigins("http://localhost:4200")
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "X-XSRF-TOKEN", "XSRF-TOKEN", "Cookie")
                .exposedHeaders("Authorization", "X-XSRF-TOKEN", "XSRF-TOKEN", "Set-Cookie")
                .allowCredentials(true);
    }
}
