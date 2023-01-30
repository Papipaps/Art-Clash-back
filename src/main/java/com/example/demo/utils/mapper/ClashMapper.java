package com.example.demo.utils.mapper;

import com.example.demo.model.data.Clash;
import com.example.demo.model.data.Profile;
import com.example.demo.model.dto.ClashDTO;
import com.example.demo.model.dto.ProfilDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
 public interface ClashMapper {


    ClashDTO toDTO(Clash save);

    Clash updateClashFromDTO(ClashDTO clashDTO, @MappingTarget Clash clash);

    Clash toEntity(ClashDTO clashDTO);
}
