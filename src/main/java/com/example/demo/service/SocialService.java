package com.example.demo.service;

import com.example.demo.model.data.Like;
import com.example.demo.model.dto.ProfilDTO;
import com.example.demo.payload.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface SocialService {

    MessageResponse followProfil(String loggedUsername, String userId);

    MessageResponse unfollowProfil(String username, String userId);

    Page<ProfilDTO> getFollowers(String userId, Pageable pageable);

    boolean likeEntity(String loggedUsername, String id, String tag);

    boolean removeLikeEntity(String loggedUsername, String id);

    Page<Like> getLikes(String id, Pageable pageable);
}
