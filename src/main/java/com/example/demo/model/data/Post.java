package com.example.demo.model.data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Table(name = "posts")
@Entity
public class Post {
    public Post() {
    }

    public Post(Long id, Long ownerId, String ownerFullname, String title, String content, Boolean anonymous, Boolean resolved, int likes, Set<Comment> comments, Date createdDate) {
        this.id = id;
        this.ownerId = ownerId;
        this.ownerFullname = ownerFullname;
        this.title = title;
        this.content = content;
        this.anonymous = anonymous;
        this.resolved = resolved;
        this.likes = likes;
        this.comments = comments;
        this.createdDate = createdDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long ownerId;
    private String ownerFullname;

    private String title;
    private String content;
    private Boolean anonymous=false;

    private Boolean resolved =false;
    private int likes;

    @OneToMany
    private Set<Comment> comments;

    private Date createdDate;

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
