package com.example.demo.service;

import com.example.demo.model.data.Media;
import com.example.demo.model.data.Profile;
import com.example.demo.model.dto.MediaDTO;
import com.example.demo.repository.MediaRepository;
import com.example.demo.repository.ProfileRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class MediaServiceImpl implements MediaService {
    private final String FOLDER_PATH = "src/main/resources/media/";
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
    public MediaDTO downloadMediafromDB(String id) throws IOException {
        Media media = fileDataRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No media found"));
        return new MediaDTO(media.getId(), media.getOwnerId(), media.getContent(), media.getCreatedDate());
    }

    @Override
    public List<String> downloadAllMediaByOwner(String ownerId, Pageable pageable) throws IOException {
        Profile profil = profilRepository.findById(ownerId).get();
        return fileDataRepository.findAllByOwnerId(profil.getId(), pageable).stream().map(Media::getId).collect(Collectors.toList());
        //List<Media> mediaIdList = fileDataRepository.findAllByOwnerId(profil.getId(), pageable);
        //List<MediaDTO> medias = new ArrayList<>();
        //mediaIdList.forEach(media -> {
        //    byte[] image = getThumbmail(media.getId());
        //    MediaDTO mediaDTO = new MediaDTO(media.getId(), profil.getId(), image,media.getCreatedDate());
        //    medias.add(mediaDTO);
        //});
        //return new PageImpl<>(medias,pageable , medias.size());
    }

    @Override
    public byte[] getThumbmail(String id) {
        Media media = fileDataRepository.findById(id).get();
        File file = new File(media.getFilePath());
        Path path = Paths.get(media.getFilePath()).getParent().getParent();
        String thumbnailPath = path +"/thumbnail/"+"thumbnail_" + media.getFilename().replace("\\", "/");
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
        }
        throw new RuntimeException("erekrfze");
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
    public String uploadImageToDB(MultipartFile file, String ownerId) throws IOException {

        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new RuntimeException("");
        }

        Profile profil = profilRepository.findByUsername(ownerId).get();

        String s = (UUID.randomUUID().toString().replace("-", "").substring(0, 15) + file.getOriginalFilename().toLowerCase().replaceAll("[^a-zA-Z0-9.]", ""));

        Media save = fileDataRepository.save(Media.builder()
                .filename(s)
                .fileType(file.getContentType())
                .content(file.getBytes())
                .createdDate(LocalDate.now())
                .ownerId(profil.getId())
                .build());
        return "file uploaded successfully !";

    }

    @Override
    public String uploadImageToFileSystem(MultipartFile file, String ownerId) throws IOException {

        initDir();
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new RuntimeException("");
        }

        Profile profil = profilRepository.findByUsername(ownerId).get();

        String s = (UUID.randomUUID().toString().replace("-", "").substring(0, 15) + file.getOriginalFilename().toLowerCase().replaceAll("[^a-zA-Z0-9.]", ""));
        String filePath = FOLDER_PATH + profil.getId() + "/storage/" + s;

        Media save = fileDataRepository.save(Media.builder()
                .filename(s)
                .fileType(file.getContentType())
                .filePath(filePath)
                .createdDate(LocalDate.now())
                .ownerId(profil.getId())
                .build());
        try {
            File outputFile = new File(filePath);
            outputFile.getParentFile().mkdirs(); // Will create parent directories if not exists
            outputFile.createNewFile();
            file.transferTo(outputFile);

        } catch (Exception exception) {
            fileDataRepository.deleteById(save.getId());
            exception.printStackTrace();
        }

        return "file uploaded successfully : " + filePath;
    }

    private void initDir() throws IOException {
        Files.createDirectories(Paths.get(FOLDER_PATH));
    }
}
