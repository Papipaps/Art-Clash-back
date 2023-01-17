package com.example.demo.controller;

import com.example.demo.model.data.Post;
import com.example.demo.model.dto.CommentDTO;
import com.example.demo.model.dto.PostDTO;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    PostService postService;

    @PostMapping("add")
    public PostDTO addPost(@RequestBody PostDTO postDTO) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        return postService.addPost(username,postDTO);
    }
    @PostMapping("comment/add")
    public CommentDTO addComment(@RequestBody CommentDTO commentDTO) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        return commentService.addComment(username,commentDTO);
    }

    @GetMapping("get/{postId}")
    public PostDTO getPost(@PathVariable String postId) {
        return postService.getPost(postId);
    }
    @DeleteMapping("delete/{postId}")
    public PostDTO deletePost(@PathVariable String postId) {
        return postService.deletePost(postId);
    }
    @GetMapping("listComment/{postId}")
    public Page<CommentDTO> getComments(@PathVariable("postId") String postId,
                                        @RequestParam(required = false, value = "size", defaultValue = "9") int size,
                                        @RequestParam(required = false, value = "page", defaultValue = "0") int page) {
        return commentService.getCommentsByPost(postId, PageRequest.of(page, size));
    }
    @GetMapping("listAllByUser/{ownerId}")
    public Page<PostDTO> getAllPost(@PathVariable("ownerId") String ownerId,
                                 @RequestParam(required = false, value = "size", defaultValue = "9") int size,
                                 @RequestParam(required = false, value = "page", defaultValue = "0") int page) {

        return postService.getAllByOwnerId(ownerId, PageRequest.of(page, size));
    }
}
