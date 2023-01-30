package com.example.demo.repository;

import com.example.demo.model.data.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeRepository extends MongoRepository<Like,String> {
    int countByEntityId(String id);
    Like findByEntityIdAndAdorerId(String entityId,String adorerId);

    Page<Like> findAllByEntityId(String ownerId, Pageable pageable);
}
