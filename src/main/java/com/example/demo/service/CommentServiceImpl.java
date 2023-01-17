package com.example.demo.service;

import com.example.demo.model.data.Comment;
import com.example.demo.model.data.Post;
import com.example.demo.model.data.Profile;
import com.example.demo.model.dto.CommentDTO;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.utils.CustomPageDTO;
import com.example.demo.utils.mapper.CommentMapper;
 import com.example.demo.utils.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Page<CommentDTO> getCommentsByPost(String postId, Pageable pageable) {
        Page<Comment> allByPostId = commentRepository.findAllByPostId(postId, pageable);
        List<CommentDTO> commentDTOS = commentMapper.toDTOs(allByPostId.getContent());
        return new PageImpl<>(commentDTOS, pageable, commentDTOS.size());
    }

    @Override
    public Page<CommentDTO> getCommentsByUser(String userId, Pageable pageable) {
        Page<Comment> allByPostId = commentRepository.findByOwnerId(userId, pageable);
        List<CommentDTO> commentDTOS = commentMapper.toDTOs(allByPostId.getContent());
        return new PageImpl<>(commentDTOS, pageable, commentDTOS.size());
    }

    @Override
    public CommentDTO getComment(String id) {
        if (id == null) {
            throw new RuntimeException("Non null required");
        }
        return commentMapper.toDTO(commentRepository.findById(id).orElse(null));
    }

    @Override
    public Page<CommentDTO> getComments(String id, Pageable pageable) {
        if (id == null) {
            throw new RuntimeException("Non null required");
        }
        Page<Comment> allByPostId = commentRepository.findAllByPostId(id, pageable);
        List<CommentDTO> commentDTOList = commentMapper.toDTOs(allByPostId.getContent());
        return new PageImpl<>(commentDTOList, pageable, commentDTOList.size());
    }

    @Override
    public CommentDTO addComment(String username, CommentDTO commentDTO) {
        Optional<Post> optPost = postRepository.findById(commentDTO.getPostId());
        if (!optPost.isPresent()) {
            throw new RuntimeException("Post does not exist");
        }
        Profile profile = profileRepository.findByUsername(username).get();
        Comment comment = Comment.builder()
                .ownerId(profile.getId())
                .createdDate(new Date())
                .message(commentDTO.getMessage())
                .post(optPost.get())
                .build();

        Post post = optPost.get();
        comment.setPost(post);

        return commentMapper.toDTO(commentRepository.save(comment));
    }

    @Override
    public CommentDTO deleteComment(String id) {
        Optional<Comment> optComment = commentRepository.findById(id);
        CommentDTO res = new CommentDTO();
        if (!optComment.isPresent()) {
            res.setErrorMessage("User not in db");
            res.setIsError(true);
            return res;
        }
        res = commentMapper.toDTO(optComment.get());
        commentRepository.deleteById(id);
        res.setMessage("comment deleted successfully !");
        return res;
    }
}
