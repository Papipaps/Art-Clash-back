package com.example.demo.controller;

import com.example.demo.model.dto.ProfilDTO;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.service.RelationshipService;
import com.example.demo.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/social")
public class SocialController {

    @Autowired
    private SocialService socialService;

    @Autowired
    private RelationshipService relationshipService;

    @PatchMapping("follow")
    private ResponseEntity<?> followProfil(@RequestParam String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MessageResponse payload = socialService.followProfil(username,userId);
        return ResponseEntity.status(HttpStatus.OK).body(payload);
    }

    @PatchMapping("followed")
    private ResponseEntity<?> unfollowProfil(@RequestParam String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MessageResponse payload = socialService.unfollowProfil(username,userId);
        return ResponseEntity.status(HttpStatus.OK).body(payload);
    }

    @GetMapping("followers/{userId}")
    private ResponseEntity<?> getFollowers(@RequestParam String userId,
                                           @RequestParam(required = false,defaultValue = "9") int size,
                                           @RequestParam(required = false,defaultValue = "0") int page){
         Page<ProfilDTO> followers =  socialService.getFollowers(userId, PageRequest.of(page,size));
        return ResponseEntity.status(HttpStatus.OK).body(followers);
    }

}
