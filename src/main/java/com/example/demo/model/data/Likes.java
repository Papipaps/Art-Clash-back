package com.example.demo.model.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class Likes {
    @Id
    private String id;
    private Long ownerId;
    private Long postId;
    private Date date;
}
