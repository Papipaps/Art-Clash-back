package com.example.demo.model.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfilDTO extends BaseDTO {
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private String backgroundId;
    private String avatarId;
    private String description;
    private String gender;
    private String email;
    private LocalDate createdDate;
    private String favColor;
    private boolean isPrivate;
    private boolean isAnonymous;
    private String country;
    private String category;
    private Date dob;
}
