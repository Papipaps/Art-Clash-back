package com.example.demo.model.data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long ownerId;
    private Date date;
   // private Post postId;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

   // public Post getPostId() {
     //   return postId;
   // }

    //public void setPostId(Post postId) {
    //    this.postId = postId;
   // }
}
