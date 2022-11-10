package com.example.demo.service;

import com.example.demo.model.data.Profil;
import com.example.demo.model.dto.ProfilDTO;
import com.example.demo.repository.ProfilRepository;
import com.example.demo.utils.ProfilMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfilServiceImp implements ProfilService {
@Autowired
    ProfilMapper profilMapper;
    @Autowired
    ProfilRepository profilRepository;




    @Override
    public List<ProfilDTO> getProfils() {
        return profilMapper.profilListToDTOs(profilRepository.findAll());
    }

    @Override
    public ProfilDTO getProfil(String username) {
        ProfilDTO res = new ProfilDTO();
        Profil profil = profilRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not in db"));
        if (profil == null) {
            res.setErrorMessage("User not in db");
            res.setIsError(true);
            return res;
        }
        res = profilMapper.profilEntityToDTO(profil);
        return res;
    }

    @Override
    public ProfilDTO updateProfil(ProfilDTO profilDTO) {
        Optional<Profil> optProfil = profilRepository.findByUsername(profilDTO.getUsername());
        if (optProfil.isEmpty()) {
            ProfilDTO res = new ProfilDTO();
            res.setErrorMessage("User not in db");
            res.setIsError(true);
            return res;
        }
        Profil profil = optProfil.get();

        return profilMapper.profilEntityToDTO(profilRepository.save(profilMapper.updateUserFromDTO(profilDTO, profil)));
    }

    @Override
    public ProfilDTO deleteProfil(Long id) {
        Profil profil = (profilRepository.findById(id).isPresent()) ? profilRepository.findById(id).get() : null;
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
        Optional<Profil> optProfil= profilRepository.findByUsername(username);
        if (optProfil.isPresent()){
            Profil profil = optProfil.get();
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
