package com.example.demo.model.data;

import com.example.demo.utils.CustomDate;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document("post")
@Data
@Builder
public class Post {

    @Id
    private String id;
    private String ownerId;
    private String ownerFullname;
    private String mediaId;
    private String title;
    private String content;
    private Boolean anonymous;
    private Boolean isDraft;
    private LocalDateTime createdDate;
    private int likes;
    private Boolean resolved;


}
