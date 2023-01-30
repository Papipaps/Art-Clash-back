package com.example.demo.repository;

import com.example.demo.model.data.Post;
import com.example.demo.model.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post,String > {
    Page<Post> findAllByOwnerId(String username, Pageable pageable);

    Page<Post> findAllByOwnerIdAndMediaIdNotNull(String ownerId, Pageable pageable);
}
