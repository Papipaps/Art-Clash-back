package com.example.demo.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Builder
public class Contestant {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    private String clashId;
    private String contestantId;
    private String mediaId;
    private boolean hasWin;
    private int currentRound;
    private int score;
    private int globalScore;
    private String mediaDescription;
    private String status;




}
