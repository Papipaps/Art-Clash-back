package com.example.demo.service;

import com.example.demo.model.data.Profile;
import com.example.demo.model.data.Relationship;
import com.example.demo.model.dto.ProfilDTO;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.RelationshipRepository;
import com.example.demo.utils.mapper.ProfilMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ProfilMapper profilMapper;


    @Override
    @Transactional
    public MessageResponse followProfil(String loggedUsername, String userId) {
        boolean res = false;
        String message;
        Profile loggedProfile = profilRepository.findByUsername(loggedUsername).get();
        Optional<Relationship> optRelationship = relationshipRepository.findByFollowedAndFollower(loggedProfile.getId(),userId);

        if (optRelationship.isEmpty()){
            Relationship relationship = Relationship.builder()
                    .followed(loggedProfile.getId())
                    .follower(userId)
                    .createdAt(new Date())
                    .state("FOLLOWED")
                    .build();
            relationshipRepository.save(relationship);
            message="Profile successfully followed.";
        }else{
            message="You are not following this profile";
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
        boolean res =false;
        String message;

        Profile loggedProfile = profilRepository.findByUsername(loggedUsername).get();
        Optional<Relationship> optRelationship = relationshipRepository.findByFollowedAndFollower(loggedProfile.getId(),userId);
        
        if (optRelationship.isPresent()){
            Relationship relationship = optRelationship.get();
            relationship.setUpdatedAt(new Date());
            relationship.setState("UNFOLLOWED");
            message="Profile successfully unfollowed.";
        }else{
            message="You are not following this profile";
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

        return new PageImpl<>(profilDTOS,pageable,profilDTOS.size());

        }

        return null;
    }
}
