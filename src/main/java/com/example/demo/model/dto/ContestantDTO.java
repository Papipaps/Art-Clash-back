package com.example.demo.model.dto;

import lombok.Builder;

@Builder
public class ContestantDTO {

        private String id;
        private String profileId;
        private String mediaId;
        private boolean hasWin;
        private int score;

}
