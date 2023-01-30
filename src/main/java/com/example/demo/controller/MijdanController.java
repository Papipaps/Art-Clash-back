package com.example.demo.controller;

import com.example.demo.model.data.Media;
import com.example.demo.repository.MediaRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("api/mijdan")
public class MijdanController {
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    ResourceLoader resourceLoader;

    @GetMapping("drip")
    public ResponseEntity<?> getDrip() throws IOException {
        Optional<Media> mijdan = mediaRepository.findByFilename("Mijdan");

        if (mijdan.isEmpty()) {

            Resource resource = resourceLoader.getResource("classpath:drip.jpg");
            InputStream inputStream = resource.getInputStream();
            // Get the output stream for the new JPEG image
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


            // Create a Thumbnails object and set the output format to JPEG
            Thumbnails.of(inputStream)
                    .size(700, 700)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

// Get the JPEG image data as a byte array
            byte[] imageData = outputStream.toByteArray();

            mediaRepository.save(Media.builder()
                    .filename("Mijdan")
                    .fileType("image/jpeg")
                    .content(imageData)
                    .createdDate(LocalDate.now())
                    .ownerId("uuid-mijdan-drip")
                    .build());
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(imageData);
        } else {
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(mijdan.get().getContent()
            );
        }
    }
}
