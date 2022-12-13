package com.example.demo.repository;

import com.example.demo.model.data.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, String> {

    Optional<Profile> findByEmail(String email);

    Optional<Profile> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
