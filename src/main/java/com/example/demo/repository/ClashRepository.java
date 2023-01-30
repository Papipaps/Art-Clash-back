package com.example.demo.repository;

import com.example.demo.model.data.Clash;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClashRepository extends MongoRepository<Clash,String> {
    Page<Clash> findAllByOwnerId(String id, Pageable pageable);

    Optional<Clash> findByOwnerId(String id);

    Page<Clash> findAllByOwnerIdAndIsFinished(String ownerId, boolean finished, Pageable pageable);
    Page<Clash> findAllByIsFinished(boolean finished, Pageable pageable);

    boolean existsByTitle(String title);
}
