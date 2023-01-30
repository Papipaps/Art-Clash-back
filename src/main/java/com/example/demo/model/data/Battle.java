package com.example.demo.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Battle {

    private @Id String id;
    private String clashId;

    private String contestantA;
    private String contestantB;

    private String winner;

    private Battle left;
    private Battle right;

    public Battle(Battle left, Battle right) {
        this.right = right;
        this.left = left;
    }

}
