package com.example.demo.model.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("comment")
public class Comment {
    @Id
     private String id;
    private Long ownerId;
    private String ownerFullName;
    private String message;
    private Date createdDate;
    @DBRef(lazy = true)
    private Post post;

    public Comment(Long ownerId, String ownerFullName, String message, Date createdDate, Post post) {
        this.ownerId = ownerId;
        this.ownerFullName = ownerFullName;
        this.message = message;
        this.createdDate = createdDate;
        this.post = post;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
