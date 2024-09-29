package com.globalitgeeks.examninja.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Allows CORS for all paths
                .allowedOrigins("http://localhost:3000/")  // Your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // HTTP methods allowed
                .allowedHeaders("*")  // All headers allowed
                .allowCredentials(true);  // Allow credentials like cookies or authentication headers
    }
}
