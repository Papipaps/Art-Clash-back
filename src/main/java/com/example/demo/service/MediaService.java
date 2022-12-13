package com.example.demo.service;

import com.example.demo.model.dto.MediaDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService {
    public byte[] downloadImageFromFileSystem(String id) throws IOException;
    public List<String> downloadAllMediaByOwner(String id, Pageable pageable) throws IOException;
    public MediaDTO downloadMediaByOwner(String id) throws IOException;

    public String uploadImageToFileSystem(MultipartFile file,String ownerId) throws IOException;


    byte[] getThumbmail(String id);

    boolean delete(String id);
}
