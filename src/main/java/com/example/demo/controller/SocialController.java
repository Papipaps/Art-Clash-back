package com.example.demo.controller;

import com.example.demo.model.data.Like;
import com.example.demo.model.dto.ProfilDTO;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.service.RelationshipService;
import com.example.demo.service.SocialService;
import com.example.demo.utils.AuthUtils;
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


    @PostMapping("follow")
    private ResponseEntity<?> followProfil(@RequestParam String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MessageResponse payload = socialService.followProfil(username,userId);
        return ResponseEntity.status(HttpStatus.OK).body(payload);
    }

    @PatchMapping("unfollow")
    private ResponseEntity<?> unfollowProfil(@RequestParam String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MessageResponse payload = socialService.unfollowProfil(username,userId);
        return ResponseEntity.status(HttpStatus.OK).body(payload);
    }

    @GetMapping("followers/{userId}")
    private ResponseEntity<?> getFollowers(@PathVariable String userId,
                                           @RequestParam(required = false,defaultValue = "9") int size,
                                           @RequestParam(required = false,defaultValue = "0") int page){
         Page<ProfilDTO> followers =  socialService.getFollowers(userId, PageRequest.of(page,size));
        return ResponseEntity.status(HttpStatus.OK).body(followers);
    }

    @PostMapping("like/{id}")
    private ResponseEntity<?> likeEntity(@PathVariable String id, @RequestParam String tag){
        boolean res = socialService.likeEntity(AuthUtils.getLoggedUsername(), id, tag);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PostMapping("like/remove/{id}")
    private ResponseEntity<?> removeLikeEntity(@PathVariable String id){
        boolean res = socialService.removeLikeEntity(AuthUtils.getLoggedUsername(),id);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("like/list/{entityId}")
    private ResponseEntity<?> getLikes(@PathVariable String entityId,
                                       @RequestParam(required = false,defaultValue = "9") int size,
                                       @RequestParam(required = false,defaultValue = "0") int page){
        return ResponseEntity.status(HttpStatus.OK).body(socialService.getLikes(entityId, PageRequest.of(page,size)));
    }

}
