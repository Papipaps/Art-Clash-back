package com.example.demo.repository;

import com.example.demo.model.data.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface LikeRepository extends JpaRepository<Like,String> {
    int countByEntityId(String id);
    Optional<Like> findByEntityIdAndAdorerId(String entityId, String adorerId);

    Page<Like> findAllByEntityId(String ownerId, Pageable pageable);
}
