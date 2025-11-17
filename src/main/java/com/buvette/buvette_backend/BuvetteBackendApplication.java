package com.buvette.buvette_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BuvetteBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuvetteBackendApplication.class, args);
    }

    // ✅ CORS configuration added from your branch
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
            	registry.addMapping("/**").allowedOrigins("http://localhost:5173");
            }
        };
    }
}
