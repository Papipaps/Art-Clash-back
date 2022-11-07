package com.example.demo.utils;

import com.example.demo.model.data.Profil;
import com.example.demo.model.dto.ProfilDTO;

public class CustomMapper {
    private ProfilMapper profilMapper;
    public ProfilDTO mapProfilPublicData(Profil profil){

        ProfilDTO profilDTO = profilMapper.profilEntityToDTO(profil);
        profilDTO.setEmail(null);
        if (profilDTO.isAnonymous()){
            profilDTO.setFirstname(null);
            profilDTO.setLastname(null);
        }
        return profilDTO;
    }
}
