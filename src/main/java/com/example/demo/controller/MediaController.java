package com.example.demo.controller;

import com.example.demo.model.dto.MediaDTO;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    MediaService mediaService;

    @PostMapping("upload")
    public ResponseEntity<?> uploadImageToFileSystem(@RequestBody MultipartFile file) throws IOException {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        String uploadImage = mediaService.uploadImageToFileSystem(file,username);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadImage);
    }
    @PostMapping("uploadToDB")
    public ResponseEntity<?> uploadImageToDB(@RequestBody MultipartFile file) throws IOException {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        return mediaService.uploadImageToDB(file,username);
    }

    @GetMapping("download/{id}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String id) throws IOException {
        byte[] imageData = mediaService.downloadImageFromFileSystem(id);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);

    }
    @GetMapping("downloadFromDB/{id}")
    public ResponseEntity<?> downloadImageFromDB(@PathVariable String id) throws IOException {
        return mediaService.downloadImageFromDB(id);

    }
    @GetMapping("downloadMetadata/{id}")
    public ResponseEntity<?> downloadImageMetaData(@PathVariable String id) throws IOException {
        MediaDTO imageData = mediaService.downloadMediaMetadata(id);
        return ResponseEntity.status(HttpStatus.OK).body(imageData);

    }

    @GetMapping("downloadAllByOwner/{ownerId}")
    public ResponseEntity<?> downloadAllByOwner(@PathVariable String ownerId, @RequestParam(defaultValue = "0",required = false) int page,@RequestParam(defaultValue = "10",required = false)int size) throws IOException {
        var imageData = mediaService.downloadAllMediaIdByOwner(ownerId, PageRequest.of(page, size));
        return ResponseEntity.status(HttpStatus.OK).body(imageData);

    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> downloadAllByOwner(@PathVariable String id) {
        boolean deleted = mediaService.delete(id);
        String payload = deleted?"Media has been deleted successfully !":"Error while deleting media with id : "+id+".";
        MessageResponse messageResponse =MessageResponse.builder().message(payload).hasError(!deleted).build();
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);

    }
    @GetMapping("getThumbnail/{id}")
    public ResponseEntity<?> getThumbmailImage(@PathVariable String id) throws IOException {
        byte[] thumbmail = mediaService.getThumbmail(id);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(thumbmail);
    }


}
