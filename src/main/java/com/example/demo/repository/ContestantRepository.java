package com.example.demo.repository;

import com.example.demo.model.data.Contestant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface ContestantRepository extends JpaRepository<Contestant, String> {
    Optional<Contestant> findByClashIdAndProfileId(String clashId, String userId);
    Optional<Contestant> findByClashId(String clashId);
    List<Contestant> findAllByClashId(String clashId);
}
