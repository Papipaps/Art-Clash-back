package com.example.demo;

import com.example.demo.model.data.Profile;
import com.example.demo.model.data.Role;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
public class DemoApplication implements CommandLineRunner {
    public static void main(java.lang.String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public void run(String... args) throws Exception {

        Role roleAdmin = roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
        Role roleUser = roleRepository.save(Role.builder().name("ROLE_USER").build());

        profileRepository.saveAll(List.of(
                        Profile.builder()
                                .username("admin")
                                .email("admin@yopmail.fr")
                                .roles(List.of(roleAdmin, roleUser))
                                .password(new BCryptPasswordEncoder().encode("secretadmin!"))
                                .createdDate(new Date())
                                .build(),
                        Profile.builder()
                                .username("username")
                                .email("user@yopmail.fr")
                                .roles(List.of(roleUser))
                                .password(new BCryptPasswordEncoder().encode("secretuser!"))
                                .createdDate(new Date())
                                .build()
                )
        );
    }

}
