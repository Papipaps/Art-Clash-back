package com.example.demo.service;

import com.example.demo.model.dto.ProfilDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProfilService  {


    List<ProfilDTO> getProfils();

    ProfilDTO getProfil(String username);


    ProfilDTO updateProfil(ProfilDTO profilDTO);

    ProfilDTO deleteProfil(String id);


    ProfilDTO getPublicProfilInformation(String username);

}
