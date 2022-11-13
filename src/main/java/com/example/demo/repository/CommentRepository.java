package com.example.demo.repository;

import com.example.demo.model.data.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment,String> {
    Comment findByOwnerId(String ownerId);
    Page<Comment> findAllByPostId(String id, Pageable pageable);
}