package com.example.demo.service;


import com.example.demo.model.data.Post;
import com.example.demo.model.dto.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService{
    Page<CommentDTO> getCommentsByPost(String postId, Pageable pageable);
    Page<CommentDTO> getCommentsByUser(String userId, Pageable pageable);
    CommentDTO getComment(String postId);
    Page<CommentDTO> getComments(String postId, Pageable pageable);

    CommentDTO addComment(String ownerId, CommentDTO commentDTO);

    CommentDTO deleteComment(String id);
}
