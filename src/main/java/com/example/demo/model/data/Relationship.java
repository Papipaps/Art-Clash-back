package com.example.demo.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Relationship {

    @Id
    private String id;
    //@DBRef(db = "profil")
    private String userId;
    //@DBRef(db = "profil")
    private String followerId;
    private Date createdAt;
    private Date updatedAt;
    private String state;
}
