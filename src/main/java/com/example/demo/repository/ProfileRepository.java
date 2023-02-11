package com.example.demo.repository;

import com.example.demo.model.data.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface ProfileRepository extends JpaRepository<Profile, String> {

    Optional<Profile> findByEmail(String email);

    Optional<Profile> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
