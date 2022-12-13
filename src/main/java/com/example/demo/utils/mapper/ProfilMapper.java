package com.example.demo.utils.mapper;

import com.example.demo.model.data.Profile;
import com.example.demo.model.dto.ProfilDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfilMapper {


    List<ProfilDTO> profilListToDTOs(List<Profile> all);

    ProfilDTO profilEntityToDTO(Profile profil);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Profile updateUserFromDTO(ProfilDTO profilDTO,@MappingTarget Profile profil);



}
