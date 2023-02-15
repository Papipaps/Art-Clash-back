package com.example.demo.service;

import com.example.demo.model.data.*;
import com.example.demo.model.dto.ProfilDTO;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.*;
import com.example.demo.utils.mapper.ProfilMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SocialServiceImpl implements SocialService {
    @Autowired
    private ProfileRepository profilRepository;
    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private ProfilMapper profilMapper;

    @Autowired
    private ClashRepository clashRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;


    @Override
    @Transactional
    public MessageResponse followProfil(String loggedUsername, String userId) {
        boolean res = false;
        String message;
        Profile loggedProfile = profilRepository.findByUsername(loggedUsername).get();
        Optional<Relationship> optRelationship = relationshipRepository.findByFollowedAndFollower(loggedProfile.getId(), userId);

        if (optRelationship.isEmpty()) {
            Relationship relationship = Relationship.builder()
                    .followed(loggedProfile.getId())
                    .follower(userId)
                    .createdAt(new Date())
                    .state("FOLLOWED")
                    .build();
            relationshipRepository.save(relationship);
            message = "Profile successfully followed.";
        } else {
            message = "You are not following this profile";
            //message="The profile you are trying to follow doesn't exist";
        }
        return MessageResponse.builder()
                .message(message)
                .hasError(!res)
                .build();
    }

    @Override
    @Transactional

    public MessageResponse unfollowProfil(String loggedUsername, String userId) {
        boolean res = false;
        String message;

        Profile loggedProfile = profilRepository.findByUsername(loggedUsername).get();
        Optional<Relationship> optRelationship = relationshipRepository.findByFollowedAndFollower(loggedProfile.getId(), userId);

        if (optRelationship.isPresent()) {
            Relationship relationship = optRelationship.get();
            relationship.setUpdatedAt(new Date());
            relationship.setState("UNFOLLOWED");
            message = "Profile successfully unfollowed.";
        } else {
            message = "You are not following this profile";
            //message="The profile you are trying to follow doesn't exist";
        }
        return MessageResponse.builder()
                .message(message)
                .hasError(!res)
                .build();
    }

    @Override
    public Page<ProfilDTO> getFollowers(String userId, Pageable pageable) {

        Optional<Profile> optProfil = profilRepository.findById(userId);

        if (optProfil.isPresent()) {

            Profile profil = optProfil.get();
            Page<Relationship> relationshipsPage = relationshipRepository.findAllByFollowed(profil.getId(), pageable);

            List<ProfilDTO> profilDTOS = new ArrayList<>();
            relationshipsPage.getContent().stream().map(Relationship::getFollower).forEach(id -> {
                Optional<Profile> p = profilRepository.findById(id);
                p.ifPresent(value -> profilDTOS.add(profilMapper.profilEntityToDTO(value)));
            });

            return new PageImpl<>(profilDTOS, pageable, profilDTOS.size());

        }

        return null;
    }

    @Override
    @Transactional
    public boolean likeEntity(String loggedUsername, String entityId, String tag) {
        if (entityId == null || entityId.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        Profile profile = profilRepository.findByUsername(loggedUsername).get();
        Optional<Like> optionalLike = likeRepository.findByEntityIdAndAdorerId(entityId, profile.getId());

        if (optionalLike.isPresent()) {
            return false;
        }

        switch (tag.toUpperCase()) {
            case "CLASH":
                Optional<Clash> clash = clashRepository.findById(entityId);
                if (clash.isPresent()) {
                    int nbLikes = clash.get().getLikes();
                    clash.get().setLikes(nbLikes + 1);
                    clashRepository.save(clash.get());
                }
                break;
            case "COMMENT":
                Optional<Comment> comment = commentRepository.findById(entityId);
                if (comment.isPresent()) {
                    int nbLikes = comment.get().getLikes();
                    comment.get().setLikes(nbLikes + 1);
                    commentRepository.save(comment.get());
                }
                break;
            case "POST":
                Optional<Post> post = postRepository.findById(entityId);
                if (post.isPresent()) {
                    int nbLikes = post.get().getLikes();
                    post.get().setLikes(nbLikes + 1);
                    postRepository.save(post.get());
                }
                break;
        }
        likeRepository.save(Like.builder()
                .entityId(entityId)
                .adorerId(profile.getId())
                .createdAt(LocalDateTime.now())
                .tag(tag)
                .build());
        return true;

    }

    @Override
    public boolean removeLikeEntity(String loggedUsername, String entityId) {
        if (entityId == null || entityId.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        Profile profile = profilRepository.findByUsername(loggedUsername).get();
        Optional<Like> optionalLike = likeRepository.findByEntityIdAndAdorerId(entityId, profile.getId());
        if (optionalLike.isEmpty()) {
            return false;
        }
        Like like = optionalLike.get();
        switch (like.getTag().toUpperCase()) {
            case "CLASH":
                Optional<Clash> clash = clashRepository.findById(entityId);
                if (clash.isPresent()) {
                    int nbLikes = clash.get().getLikes();
                    clash.get().setLikes(nbLikes - 1);
                    clashRepository.save(clash.get());
                }
                break;
            case "COMMENT":
                Optional<Comment> comment = commentRepository.findById(entityId);
                if (comment.isPresent()) {
                    int nbLikes = comment.get().getLikes();
                    comment.get().setLikes(nbLikes - 1);
                    commentRepository.save(comment.get());
                }
                break;
            case "POST":
                Optional<Post> post = postRepository.findById(entityId);
                if (post.isPresent()) {
                    int nbLikes = post.get().getLikes();
                    post.get().setLikes(nbLikes - 1);
                    postRepository.save(post.get());
                }
                break;
        }
        likeRepository.deleteById(like.getId());
        return true;

    }

    @Override
    public Page<Like> getLikes(String id, Pageable pageable) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        return likeRepository.findAllByEntityId(id, pageable);
    }
}
