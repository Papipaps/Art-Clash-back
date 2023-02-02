package com.example.demo.model.dto;

import com.example.demo.model.data.Battle;
import com.example.demo.model.data.Like;
import com.example.demo.model.data.Podium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ClashDTO extends BaseDTO {


    private String id;
    private String ownerId;
    private String ownerName;
    private String title;
    private String theme;
    private String description;
    private List<Battle> contestants;
    private LocalDateTime createdDate;
    private Podium podium;
    private boolean isFinished;
    private int likes;
    private int round;
    private int currentRound;


}
