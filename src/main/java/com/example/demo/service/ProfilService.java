package com.example.demo.service;

import com.example.demo.model.dto.ProfilDTO;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProfilService  {


    List<ProfilDTO> getProfils();

    ProfilDTO getProfil(String username,@DefaultValue(value = "true") boolean isCurrent);


    ProfilDTO updateProfil(ProfilDTO profilDTO);

    ProfilDTO deleteProfil(String id);


    ProfilDTO getPublicProfilInformation(String username);

}
