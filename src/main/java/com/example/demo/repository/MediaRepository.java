package com.example.demo.repository;

import com.example.demo.model.data.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface MediaRepository extends JpaRepository<Media,String> {
    Page<Media> findAllByOwnerId(String id, Pageable pageable);
    Optional<Media> findByOwnerId(String id);
    Optional<Media> findByFilename(String name);

    int countAllByOwnerId(String id);
}
