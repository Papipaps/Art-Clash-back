package com.example.demo;

import com.example.demo.controller.AuthController;
import com.example.demo.model.data.ERole;
import com.example.demo.model.data.Profile;
import com.example.demo.model.data.Role;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.repository.*;
import com.example.demo.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.demo.repository")
public class DemoApplication {
    final java.lang.String LOCAL_HOST_URL = "http://127.0.0.1:3000/";

    public static void main(java.lang.String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000",
                "3.75.158.163",
                "3.125.183.140",
                "35.157.117.28",
                "https://artclash-service.onrender.com"));
        configuration.setAllowedMethods(List.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
