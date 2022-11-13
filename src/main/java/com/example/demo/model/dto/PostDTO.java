package com.example.demo.model.dto;

public class PostDTO extends BaseDTO{
    private String id;
    private Long ownerId;
    private String ownerFullname;

    private String title;
    private String content;

    private Boolean anonymous=false;

    private Boolean resolved =false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
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
