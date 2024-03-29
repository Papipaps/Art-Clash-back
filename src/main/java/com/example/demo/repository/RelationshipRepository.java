package com.example.demo.repository;

import com.example.demo.model.data.Relationship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface RelationshipRepository extends JpaRepository<Relationship,String> {
    Page<Relationship> findAllByFollowed(String followerId,Pageable pageable);

    Optional<Relationship> findByFollowedAndFollower(String loggedProfil, String userId);
}
