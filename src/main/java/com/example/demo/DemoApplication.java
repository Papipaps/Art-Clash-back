package com.example.demo;

import com.example.demo.model.data.Profile;
import com.example.demo.model.data.Role;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.demo.repository")
public class DemoApplication  {
    public static void main(java.lang.String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

/*
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.existsById("UUID-ROLE-USER") && roleRepository.existsById("UUID-ROLE-ADMIN")) {
            Role roleUser = new Role();
            roleUser.setId("UUID-ROLE-USER");
            roleUser.setName("ROLE_USER");
            roleRepository.save(roleUser);

            Role roleAdmin = new Role();
            roleAdmin.setId("UUID-ROLE-ADMIN");
            roleAdmin.setName("ROLE_ADMIN");
            roleRepository.save(roleAdmin);

            if (!profileRepository.existsByUsername("admin") && !profileRepository.existsByUsername("username")) {

                profileRepository.saveAll(List.of(
                                Profile.builder()
                                        .username("admin")
                                        .roles(List.of(roleAdmin, roleUser))
                                        .password(new BCryptPasswordEncoder().encode("secretadmin!"))
                                        .createdDate(new Date())
                                        .build(),
                                Profile.builder()
                                        .username("username")
                                        .roles(List.of(roleUser))
                                        .password(new BCryptPasswordEncoder().encode("secretuser!"))
                                        .createdDate(new Date())
                                        .build()
                        )
                );
            }
        }

    }*/
}
