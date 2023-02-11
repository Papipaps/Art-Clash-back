package com.example.demo.repository;

import com.example.demo.model.data.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CommentRepository extends JpaRepository<Comment,String> {
    Page<Comment> findByOwnerId(String ownerId, Pageable pageable);
}