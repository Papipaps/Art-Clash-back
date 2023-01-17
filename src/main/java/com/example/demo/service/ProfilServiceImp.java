package com.example.demo.service;

import com.example.demo.model.data.Profile;
import com.example.demo.model.dto.ProfilDTO;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.utils.mapper.ProfilMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfilServiceImp implements ProfilService {
@Autowired
    ProfilMapper profilMapper;
    @Autowired
    ProfileRepository profilRepository;




    @Override
    public List<ProfilDTO> getProfils() {
        return profilMapper.profilListToDTOs(profilRepository.findAll());
    }

    @Override
    public ProfilDTO getProfil(String username, boolean isCurrent) {
        ProfilDTO res = new ProfilDTO();
        Optional<Profile> profil = isCurrent? profilRepository.findByUsername(username):profilRepository.findById(username);
        if (profil.isEmpty()) {
            res.setErrorMessage("User not in db");
            res.setIsError(true);
            return res;
        }
        res = profilMapper.profilEntityToDTO(profil.get());
        return res;
    }

    @Override
    public ProfilDTO updateProfil(ProfilDTO profilDTO) {
        Optional<Profile> optProfil = profilRepository.findByUsername(profilDTO.getUsername());
        if (optProfil.isEmpty()) {
            ProfilDTO res = new ProfilDTO();
            res.setErrorMessage("User not in db");
            res.setIsError(true);
            return res;
        }
        Profile profil = optProfil.get();

        return profilMapper.profilEntityToDTO(profilRepository.save(profilMapper.updateUserFromDTO(profilDTO, profil)));
    }

    @Override
    public ProfilDTO deleteProfil(String id) {
        Profile profil = (profilRepository.findById(id).isPresent()) ? profilRepository.findById(id).get() : null;
        if (profil == null) {
            ProfilDTO res = new ProfilDTO();
            res.setErrorMessage("User not in db");
            res.setIsError(true);
            return res;
        }
        profilRepository.deleteById(id);
        ProfilDTO res = profilMapper.profilEntityToDTO(profil);
        res.setErrorMessage("User deleted succesfully !");
        return res;
    }

    @Override
    public ProfilDTO getPublicProfilInformation(String username) {
        Optional<Profile> optProfil= profilRepository.findByUsername(username);
        if (optProfil.isPresent()){
            Profile profil = optProfil.get();
            ProfilDTO profilDTO = profilMapper.profilEntityToDTO(profil);
            profilDTO.setEmail(null);
            if (profilDTO.isAnonymous()){
                profilDTO.setFirstname(null);
                profilDTO.setLastname(null);
            }
            return profilDTO;
        }
        ProfilDTO res = new ProfilDTO();
        res.setErrorMessage("User not in db");
        res.setIsError(true);
        return res;
    }


}
