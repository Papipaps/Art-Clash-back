package com.example.demo.repository;

import com.example.demo.model.data.Clash;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository


public interface ClashRepository extends JpaRepository<Clash,String> {
    Page<Clash> findAllByOwnerId(String id, Pageable pageable);

    Optional<Clash> findByOwnerId(String id);

    boolean existsByTitle(String title);

    Page<Clash> findAllByOwnerIdAndRestricted(String id, boolean restricted, Pageable pageable);

    Page<Clash> findAllByRestricted(boolean restricted, Pageable pageable);

    Page<Clash> findAllByStatus(String status, Pageable pageable);

}
