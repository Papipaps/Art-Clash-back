package com.example.demo.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@Builder
public class Clash {

    private @Id String id;
    private String ownerId;
    private String title;
    private String theme;
    private String description;
    private List<Battle> contestants;
    private LocalDateTime createdDate;
    private boolean isFinished;
    private int likes;
    private int round;
    private int currentRound;
    private Podium podium;


}
