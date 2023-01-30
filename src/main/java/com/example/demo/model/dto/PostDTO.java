package com.example.demo.model.dto;

import com.example.demo.model.data.Media;
import com.example.demo.utils.CustomDate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO extends BaseDTO {
    private String id;
    private String ownerId;
    private String ownerFullname;
    private String title;
    private String content;
    private Boolean anonymous;
    private Boolean resolved;
    private Boolean isDraft;
    private CustomDate postedAt;
    private String mediaId;
    private LocalDateTime createdDate;



}
