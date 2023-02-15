package com.example.demo.model.dto;

import com.example.demo.model.data.Contestant;
import lombok.Builder;
import lombok.Data;

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
    private List<ContestantDTO> contestants;
    private LocalDateTime createdDate;
    private boolean isFinished;
    private int likes;
    private int round;
    private int currentRound;
    private boolean restricted;
    private String status;
    private int slot;


}
