package com.adrian.bookstoreapi.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// // CORS config to allow Frontend Client requests - basta con esta cnofig, no hace falta nada mas
@Configuration
public class MvcConfig {

    @Value("${app.client.frontend-url}")
    private String frontUrl;


    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(frontUrl)
                        .allowedMethods("*")
                        .exposedHeaders("*");
            }
        };
    }

}
