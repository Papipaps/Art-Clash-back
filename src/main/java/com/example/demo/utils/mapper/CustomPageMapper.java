package com.example.demo.utils.mapper;

import com.example.demo.model.data.Media;
import com.example.demo.model.dto.MediaDTO;
import com.example.demo.utils.CustomPageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomPageMapper {
    @Autowired
    MediaMapper mediaMapper;
    public CustomPageDTO<MediaDTO> pageToPageDTO(Page<Media> mediaPage){
        List<MediaDTO> mediaDTOS = mediaMapper.toDTOs(mediaPage.getContent());
        PageImpl<MediaDTO> dtos = new PageImpl<>(mediaDTOS, mediaPage.getPageable(), mediaDTOS.size());

        return new CustomPageDTO<>(dtos);
    }
}
