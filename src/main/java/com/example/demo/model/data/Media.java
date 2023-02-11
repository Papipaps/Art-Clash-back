package com.example.demo.model.data;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.InputStream;
import java.time.LocalDate;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Media {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
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

}
