package com.example.demo.model.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@Document("comment")
@Data
public class Comment {
    @Id
     private String id;
    private String ownerId;
    private String ownerFullName;
    private String message;
    private Date createdDate;
    @DBRef(lazy = true)
    private Post post;

    private int likes;


}
