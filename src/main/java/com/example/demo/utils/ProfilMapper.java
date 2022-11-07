package com.example.demo.utils;

import com.example.demo.model.data.Profil;
import com.example.demo.model.dto.ProfilDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfilMapper {


    List<ProfilDTO> profilListToDTOs(List<Profil> all);

    ProfilDTO profilEntityToDTO(Profil profil);

    Profil updateUserFromDTO(ProfilDTO profilDTO,@MappingTarget Profil profil);


}
