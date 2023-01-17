package com.example.demo.service;


import com.example.demo.model.data.Media;
import com.example.demo.model.data.Post;
import com.example.demo.model.data.Profile;
import com.example.demo.model.dto.PostDTO;
import com.example.demo.model.dto.ProfilDTO;
import com.example.demo.repository.MediaRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.utils.CustomDate;
import com.example.demo.utils.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ProfilService profilService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private PostMapper postMapper;

    @Override
    public PostDTO getPost(String id) {
        if (id == null || id.isEmpty()) {
            throw new RuntimeException("Cannot be null");
        }
        Post post = postRepository.findById(id).get();
        Profile profil = profileRepository.findById(post.getOwnerId()).get();


        PostDTO postDTO = postMapper.toDTO(post);

        if(post.getMedia()!=null){
            Media media = mediaRepository.findById(post.getMedia().getId()).orElseGet(() -> null);
            postDTO.setMedia(media);
        }

        postDTO.setOwnerFullname(profil.isAnonymous() ? profil.getUsername() : profil.getFirstname().concat(" ").concat(profil.getLastname()));

        LocalDateTime createdDate = post.getCreatedDate();
        int year = createdDate.getYear();
        int month = createdDate.getMonthValue();
        int day = createdDate.getDayOfMonth();
        int hour = createdDate.getHour();
        int minute = createdDate.getMinute();
        int second = createdDate.getSecond();
        postDTO.setPostedAt(CustomDate.builder()
                .day(day)
                .month(month)
                .year(year)
                .hour(hour)
                .minute(minute)
                .second(second).build());
        return postDTO;
    }

    @Override
    public PostDTO addPost(String username, PostDTO postDTO) {
        Post post = postMapper.toEntity(postDTO);
        Profile profil = profileRepository.findByUsername(username).get();

        post.setCreatedDate(LocalDateTime.now());
        post.setOwnerId(profil.getId());
        post.setOwnerFullname(profil.getFirstname() + " " + profil.getLastname());

        PostDTO res = postMapper.toDTO(postRepository.save(post));
        LocalDateTime createdDate = post.getCreatedDate();
        int year = createdDate.getYear();
        int month = createdDate.getMonthValue();
        int day = createdDate.getDayOfMonth();
        int hour = createdDate.getHour();
        int minute = createdDate.getMinute();
        int second = createdDate.getSecond();
        postDTO.setPostedAt(CustomDate.builder()
                .day(day)
                .month(month)
                .year(year)
                .hour(hour)
                .minute(minute)
                .second(second).build());
        return postDTO;
    }


    @Override
    public PostDTO deletePost(String id) {
        if (id == null) {
            throw new RuntimeException("Not null");
        }
        Post post = (postRepository.findById(id).isPresent()) ? postRepository.findById(id).get() : null;
        if (post == null) {
            PostDTO res = new PostDTO();
            res.setErrorMessage("Post not in db");
            res.setIsError(true);
            return res;
        }
        postRepository.deleteById(id);
        PostDTO res = postMapper.toDTO(post);
        res.setContent("Post deleted succesfully !");
        return res;
    }

    @Override
    public Page<PostDTO> getAllByOwnerId(String ownerId, Pageable pageable) {
        Page<Post> allByOwnerId = postRepository.findAllByOwnerId(ownerId, pageable);
        List<PostDTO> postDTOList = allByOwnerId.getContent().stream().map(post -> {

            PostDTO postDTO = postMapper.toDTO(post);
            Profile profil = profileRepository.findById(ownerId).get();

            if(post.getMedia()!=null){
                Media media = mediaRepository.findById(post.getMedia().getId()).orElseGet(() -> null);
                postDTO.setMedia(media);
            }

            postDTO.setOwnerFullname(profil.isAnonymous() ? profil.getUsername() : profil.getFirstname().concat(" ").concat(profil.getLastname()));

            LocalDateTime createdDate = post.getCreatedDate();
            int year = createdDate.getYear();
            int month = createdDate.getMonthValue();
            int day = createdDate.getDayOfMonth();
            int hour = createdDate.getHour();
            int minute = createdDate.getMinute();
            int second = createdDate.getSecond();
            postDTO.setPostedAt(CustomDate.builder()
                    .day(day)
                    .month(month)
                    .year(year)
                    .hour(hour)
                    .minute(minute)
                    .second(second).build());
            return postDTO;
        }).collect(Collectors.toList());
        return new PageImpl<>(postDTOList, pageable, postDTOList.size());

    }
}
