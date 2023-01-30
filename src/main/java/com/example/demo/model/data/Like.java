package com.example.demo.model.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@Builder
public class Like {
    @Id
    private String id;
    private String entityId;
    private String adorerId;
    private LocalDateTime createdAt;
    private String tag;
}
