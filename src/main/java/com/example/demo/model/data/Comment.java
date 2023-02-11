package com.example.demo.model.data;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
public class Comment {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
     private String id;
    private String ownerId;
    private String ownerFullName;
    private String message;
    private Date createdDate;
    private int likes;


}
