package com.example.demo.controller;

import com.example.demo.model.data.Media;
import com.example.demo.model.data.Profil;
import com.example.demo.model.dto.MediaDTO;
import com.example.demo.repository.MediaRepository;
import com.example.demo.repository.ProfilRepository;
import com.example.demo.utils.CustomPageDTO;
import com.example.demo.utils.mapper.CustomPageMapper;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    MediaRepository mediaRepository;
    @Autowired
    ProfilRepository profilRepository;

    @Autowired
    CustomPageMapper mediaMapper;
@PostMapping("/upload")
    public ResponseEntity<?> uploadMedia(@RequestParam MultipartFile file) throws Exception{
    if (file==null){
        throw new FileUploadException("File must not be null.");

    }
    if (!file.getContentType().equals("image/jpeg")||!file.getContentType().equals("image/jpeg")){
        throw new FileUploadException("File must be of png/jpeg type !");
    }
    Media media=new Media();
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Profil profil = profilRepository.findByUsername(username).get();
    media.setFilename("attachment_"+profil.getUsername()+"_"+file.getOriginalFilename());
    media.setContent(file.getBytes());
    media.setFileType(file.getContentType());
    media.setFileSize(file.getSize());
    media.setOwnerId(profil.getId());
    mediaRepository.save(media);
    return ResponseEntity.ok().body("Image uploaded");
}

@GetMapping("/download/{id}")
    public MediaDTO downloadFile(@PathVariable("id") String id){
    Media media=mediaRepository.findById(id).orElseThrow(() -> new RuntimeException("No media with ID "+id));
    String downloadURL= ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/attachment/").path(media.getId()).toUriString();
    return new MediaDTO(downloadURL,media.getFilename(),media.getFileSize(),media.getFileType());
}

    @GetMapping("/downloadAllById/{id}")
    public CustomPageDTO<MediaDTO> downloadAllFile(@PathVariable("id") String id,
                                          @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Media> mediaPage = mediaRepository.findAllByOwnerId(id, pageable);
        return  mediaMapper.pageToPageDTO(mediaPage);

    }

}
