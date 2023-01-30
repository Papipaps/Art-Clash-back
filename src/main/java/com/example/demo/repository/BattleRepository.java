package com.example.demo.repository;

import com.example.demo.model.data.Battle;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BattleRepository extends MongoRepository<Battle, String> {
    Optional<Battle> findByClashId(String postId);
}
