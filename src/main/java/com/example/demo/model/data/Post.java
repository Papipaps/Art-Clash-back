package com.example.demo.model.data;

import com.example.demo.utils.CustomDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    private String ownerId;
    private String ownerFullname;
    private String mediaId;
    private String title;
    private String content;
    private Boolean anonymous;
    private Boolean isDraft;
    private LocalDateTime createdDate;

    @OneToMany(targetEntity = Comment.class)
    private Collection<Comment> comment;
    private int likes;
    private boolean resolved;


}
