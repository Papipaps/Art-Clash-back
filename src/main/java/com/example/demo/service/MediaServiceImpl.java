package com.example.demo.service;

import com.example.demo.model.data.Media;
import com.example.demo.model.data.Post;
import com.example.demo.model.data.Profile;
import com.example.demo.model.dto.MediaDTO;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.MediaRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.utils.mapper.PostMapper;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service

public class MediaServiceImpl implements MediaService {
    //private final String FOLDER_PATH = Paths.get("").toAbsolutePath().getParent().toString().concat("\\storage\\media\\");

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MediaRepository fileDataRepository;

    @Autowired
    private ProfileRepository profilRepository;

    @Override
    public byte[] downloadImageFromFileSystem(String id) throws IOException {
        Optional<Media> fileData = fileDataRepository.findById(id);
        String filePath = fileData.get().getFilePath();
        byte[] image = Files.readAllBytes(new File(filePath).toPath());
        return image;
    }

    @Override
    public ResponseEntity<byte[]> downloadImageFromDB(String id) throws IOException {
        Optional<Media> fileData = fileDataRepository.findById(id);
        byte[] imageData = fileData.get().getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    @Override
    public MediaDTO downloadMediaMetadata(String id) throws IOException {
        Media media = fileDataRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No media found"));
        return MediaDTO.builder()
                .id(media.getId())
                .ownerId(media.getOwnerId())
                .createdDate(media.getCreatedDate())
                .build();
    }

    @Override
    public Page<Media> downloadAllMediaIdByOwner(String ownerId, Pageable pageable) throws IOException {
        Profile profil = profilRepository.findById(ownerId).get();
        return fileDataRepository.findAllByOwnerId(profil.getId(), pageable);
    }

    @Override
    public byte[] getThumbmail(String id) {
        Media media = fileDataRepository.findById(id).get();
        File file = new File(media.getFilePath());
        Path path = Paths.get(media.getFilePath()).getParent().getParent();
        String thumbnailPath = path + "/thumbnail/" + "thumbnail_" + media.getFilename().replace("\\", "/");
        File thumbnailFile = new File(thumbnailPath);
        media.setThumbnailPath(thumbnailPath);
        try {
            thumbnailFile.getParentFile().mkdirs(); // Will create parent directories if not exists
            thumbnailFile.createNewFile();
            Media save = fileDataRepository.save(media);
            Thumbnails.of(file).size(400, 400).toFile(thumbnailFile);
            return Files.readAllBytes(new File(media.getThumbnailPath()).toPath());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("erekrfze");
        }
    }

    @Override
    public boolean delete(String id) {
        boolean res = false;
        Optional<Media> optMedia = fileDataRepository.findById(id);
        if (optMedia.isPresent()) {
            Media media = optMedia.get();
            try {
                // Delete the file
                Path path = Paths.get(media.getFilePath());
                Path path1 = Paths.get(media.getThumbnailPath());
                Files.delete(path);
                Files.delete(path1);
                fileDataRepository.deleteById(id);
                res = true;
            } catch (Exception e) {
                System.out.println("Error deleting file: " + e.getMessage());
            }
        }
        return res;
    }

    @Override
    public ResponseEntity<?> uploadImageToDB(MultipartFile file, String loggedUsername) throws IOException {

        Profile profil = profilRepository.findByUsername(loggedUsername).get();

        if (fileDataRepository.countAllByOwnerId(profil.getId()) >= 20) {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.builder().message("Can't upload more than 20 images.").hasError(true).build());
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new RuntimeException("");
        }
        if (file.getContentType() == null || file.getContentType().isEmpty()) {
            throw new RuntimeException("");
        }

        String s = UUID.randomUUID().toString().concat("." + file.getContentType().split("/")[1]);

        // Get the input stream for the file
        InputStream inputStream = file.getInputStream();

        // Get the output stream for the new JPEG image
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Create a Thumbnails object and set the output format to JPEG
        Thumbnails.of(inputStream)
                .size(700, 700)
                .outputFormat("jpg")
                .toOutputStream(outputStream);

// Get the JPEG image data as a byte array
        byte[] imageData = outputStream.toByteArray();

        Media save = fileDataRepository.save(Media.builder()
                .filename(s)
                .fileType(file.getContentType())
                .content(imageData)
                .createdDate(LocalDate.now())
                .ownerId(profil.getId())
                .fileSize(file.getSize())
                .build());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageResponse.builder()
                    .message("file uploaded successfully with id : " + save.getId())
                    .payload(save.getId())
                    .build());

    }
@Autowired
    PostMapper postMapper;
    @Override
    public String uploadImageToFileSystem(MultipartFile file, String ownerId) throws IOException {
        return "";
        //    initDir();
        //    if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
        //        throw new RuntimeException("");
        //    }

        //    Profile profil = profilRepository.findByUsername(ownerId).get();

        //    Media save = fileDataRepository.save(Media.builder()
        //            .fileType(file.getContentType())
        //            .createdDate(LocalDate.now())
        //            .ownerId(profil.getId())
        //            .build());
        //    String s = UUID.randomUUID().toString().concat("." + save.getFileType().split("/")[1]);
        //    save.setFilename(s);
        //    String filePath = FOLDER_PATH + profil.getId() + File.separator + s;
        //    save.setFilePath(filePath);
        //    fileDataRepository.save(save);

        //    try {
        //        File outputFile = new File(filePath);
        //        outputFile.getParentFile().mkdirs(); // Will create parent directories if not exists
        //        file.transferTo(outputFile);
        //        Thumbnails.of(outputFile).size(1000, 1000).toFile(outputFile);

        //    } catch (Exception exception) {
        //        fileDataRepository.deleteById(save.getId());
        //        exception.printStackTrace();
        //    }

        //    return "file uploaded successfully : " + filePath;
    }

    private void initDir() throws IOException {
        //    Files.createDirectories(Paths.get(FOLDER_PATH));
    }
}
