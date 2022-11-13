package com.example.demo.model.data;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Document("post")
public class Post {
    public Post() {
    }

    public Post(String id, Long ownerId, String ownerFullname, String title, String content, Boolean anonymous, Boolean resolved, Date createdDate) {
        this.id = id;
        this.ownerId = ownerId;
        this.ownerFullname = ownerFullname;
        this.title = title;
        this.content = content;
        this.anonymous = anonymous;
        this.resolved = resolved;
        this.createdDate = createdDate;
    }

    @Id
    private String id;
    private Long ownerId;
    private String ownerFullname;

    private String title;
    private String content;

    private Boolean anonymous=false;

    private Boolean resolved =false;

    private Date createdDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerFullname() {
        return ownerFullname;
    }

    public void setOwnerFullname(String ownerFullname) {
        this.ownerFullname = ownerFullname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
