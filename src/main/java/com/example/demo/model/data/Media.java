package com.example.demo.model.data;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.InputStream;
import java.time.LocalDate;

@Document
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Media {
    @Id
private String id;
private String filePath;
private String filename;
private String fileType;
private InputStream stream;
private long fileSize;
private String ownerId;
private String tag;
private String thumbnailPath;
private LocalDate createdDate;

}
