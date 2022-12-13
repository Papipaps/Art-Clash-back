package com.example.demo.repository;

import com.example.demo.model.data.Relationship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RelationshipRepository extends MongoRepository<Relationship,String> {
    Page<String> findAllByUserId(String followerId, Pageable pageable);

    Optional<Relationship> findByUserIdAndFollowerId(String loggedProfil, String userId);
}
