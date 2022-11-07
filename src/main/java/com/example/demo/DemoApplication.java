package com.example.demo;

import com.example.demo.controller.AuthController;
import com.example.demo.model.data.ERole;
import com.example.demo.model.data.Role;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.repository.ProfilRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashSet;
import java.util.List;


@SpringBootApplication
public class DemoApplication {
    final String LOCAL_HOST_URL = "http://127.0.0.1:3000/";

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    CommandLineRunner run(RoleRepository roleRepository, AuthController authService, ProfilRepository profilRepository) throws Exception {
        return args -> {

            roleRepository.save(new Role(ERole.ROLE_ADMIN));
            SignupRequest adminRequest = new SignupRequest();
            adminRequest.setFirstname("userfirst");
            adminRequest.setLastname("userlast");
            adminRequest.setEmail("user@mail.fr");
            adminRequest.setUsername("admin");
            adminRequest.setPassword("password");
            adminRequest.setRole(new HashSet<>(List.of("admin")));

            authService.registerUser(adminRequest);
        };
    }

}
