package com.example.demo.model.data;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
public class Clash {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    private String ownerId;
    private String title;
    private String theme;
    private String description;
//    @ManyToMany
//    private Collection<Contestant> contestants;
    private LocalDateTime createdDate;
    private boolean restricted;
    private String status;
    private int likes;
    private int round;
    private String first;
    private String second;
    private String third;
    private int currentRound;
    private int slot;


//    private String[] artists;

}
