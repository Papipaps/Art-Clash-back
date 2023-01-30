package com.example.demo.service;


import com.example.demo.model.data.Post;
import com.example.demo.model.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PostService {

    PostDTO getPost(String id);
    PostDTO addPost(String username, PostDTO postDTO);
    PostDTO deletePost(String id);

    Page<PostDTO> getAllByOwnerId(String ownerId, Pageable pageable, boolean isMedia);
}
