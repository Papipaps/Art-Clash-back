package com.example.demo.repository;


import com.example.demo.model.data.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilRepository extends JpaRepository<Profil,String> {

    Optional<Profil> findByEmail(String email);

    Optional<Profil> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
