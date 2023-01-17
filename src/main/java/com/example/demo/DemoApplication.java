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
    CommandLineRunner run(PasswordEncoder passwordEncoder, RoleRepository roleRepository, ProfileRepository profilRepository, AuthController profilService, RoleService roleService, CommentRepository commentRepository, PostRepository postRepository) throws Exception {
        return args -> {
            if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                roleService.saveRole(new Role(ERole.ROLE_ADMIN.name()));
            }
            if (roleRepository.findByName("ROLE_USER").isEmpty()) {
                roleService.saveRole(new Role(ERole.ROLE_USER.name()));
            }
            if (!profilRepository.existsByUsername("admin")) {

                SignupRequest adminRequest = new SignupRequest();
                adminRequest.setFirstname("firstname");
                adminRequest.setLastname("lastname");
                adminRequest.setEmail("admin@mail.fr");
                adminRequest.setUsername("admin");
                adminRequest.setPassword("password");
                profilService.registerUser(adminRequest);
                roleService.addRoleToUser("admin", ERole.ROLE_ADMIN.name());
            }
            for (int i = 0; i < 10 ; i++) {
                if(!profilRepository.existsByUsername("dummy"+i))
                    profilRepository.save(
                                Profile.builder()
                                        .email("dummy"+i+"@yopmail.com")
                                        .createdDate(new Date())
                                        .firstname("first"+i)
                                        .lastname("last"+i)
                                        .username("dummy"+i)
                                        .password(passwordEncoder.encode("password"))
                                        .build());
            }
        };
    }

}
