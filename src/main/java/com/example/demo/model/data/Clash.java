package com.example.demo.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @OneToMany
    @JoinTable(
            name = "clash_contestant",
            joinColumns = @JoinColumn(name = "clash_id"),
            inverseJoinColumns = @JoinColumn(name = "contestant_id"))
    private Collection<Contestant> contestants;
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

}
