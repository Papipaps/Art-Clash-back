package com.example.demo.repository;

import com.example.demo.model.data.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MediaRepository extends MongoRepository<Media,String> {
    Page<Media> findAllByOwnerId(String id, Pageable pageable);
}
