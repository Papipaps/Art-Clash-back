package com.example.demo.utils.mapper;

import com.example.demo.model.data.Media;
import com.example.demo.model.dto.MediaDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaMapper { 
    MediaDTO toDTO(Media media);
    Media toEntity(MediaDTO mediaDTO);

    List<MediaDTO> toDTOs(List<Media> mediaPage);
}
