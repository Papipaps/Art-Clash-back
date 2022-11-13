package com.example.demo.controller;

import com.example.demo.model.data.Comment;
import com.example.demo.model.dto.CommentDTO;
import com.example.demo.model.dto.PostDTO;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.utils.mapper.CommentMapper;
import com.example.demo.utils.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;
@Autowired
PostRepository postRepository;

@Autowired
    PostMapper postMapper;
    @Autowired
    CommentMapper commentMapper;

    @GetMapping("/getListbyId")
    public Page<CommentDTO> getComments(@RequestParam("postId") String id, @RequestParam(value = "size", defaultValue = "9") int size, @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> allByPostId = commentRepository.findAllByPostId(id, pageable);
        List<CommentDTO> commentDTOList = commentMapper.toDTOs(allByPostId.getContent());
        return new PageImpl<>(commentDTOList, pageable, commentDTOList.size());
    }
    @GetMapping("/")
    public List<PostDTO> getComment( ) {

    return postMapper.toDTOs(postRepository.findAll());}
}
