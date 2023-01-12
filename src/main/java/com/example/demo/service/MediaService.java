package com.example.demo.service;

import com.example.demo.model.dto.MediaDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService {
    byte[] downloadImageFromFileSystem(String id) throws IOException;

    MediaDTO downloadMediaMetadata(String id) throws IOException;

    String uploadImageToFileSystem(MultipartFile file, String ownerId) throws IOException;

    String uploadImageToDB(MultipartFile file, String ownerId) throws IOException;

    List<String> downloadAllMediaByOwner(String id, Pageable pageable) throws IOException;


    byte[] getThumbmail(String id);

    boolean delete(String id);
}
