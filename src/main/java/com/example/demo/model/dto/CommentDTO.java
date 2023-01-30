package com.example.demo.model.dto;

import lombok.Data;

import java.util.Date;
@Data
public class CommentDTO extends BaseDTO {
    private String id;
    private String message;
    private Date createdDate;
    private Long ownerId;
    private String ownerFullName;
    private String postId;
    private int likes;
}
