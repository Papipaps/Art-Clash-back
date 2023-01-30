package com.example.demo.repository;

import com.example.demo.model.data.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends MongoRepository<Media,String> {
    Page<Media> findAllByOwnerId(String id, Pageable pageable);
    Optional<Media> findByOwnerId(String id);
    Optional<Media> findByFilename(String name);

    int countAllByOwnerId(String id);
}
