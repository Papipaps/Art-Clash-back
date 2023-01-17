package com.example.demo.model.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.InputStream;
import java.time.LocalDate;

@Document
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Media {
    @Id
    private String id;
    private String filePath;
    private String filename;
    private String fileType;
    private byte[] content;
    private long fileSize;
    private String ownerId;
    private String tag;
    private String thumbnailPath;
    private LocalDate createdDate;
    @DBRef(db = "post")
    private Post post;

}
