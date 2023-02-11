package com.example.demo.service;

import com.example.demo.model.data.Media;
import com.example.demo.model.dto.MediaDTO;
import com.example.demo.payload.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaService {
    byte[] downloadImageFromFileSystem(String id) throws IOException;
    ResponseEntity<byte[]> downloadImageFromDB(String id) throws IOException;

    MediaDTO downloadMediaMetadata(String id) throws IOException;

    String uploadImageToFileSystem(MultipartFile file, String ownerId) throws IOException;

    MessageResponse uploadImageToDB(MultipartFile file, String loggedUsername) throws IOException;

    Page<Media> downloadAllMediaIdByOwner(String id, Pageable pageable) throws IOException;

    byte[] getThumbmail(String id);

    boolean delete(String id);
}
