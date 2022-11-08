package com.example.demo.controller;

import com.example.demo.model.data.Profil;
import com.example.demo.model.dto.ProfilDTO;
import com.example.demo.repository.ProfilRepository;
import com.example.demo.service.ProfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/profil")
public class ProfilController {

    @Autowired
    ProfilService profilService;

    @Autowired
    ProfilRepository profilRepository;


    @PutMapping(path = "update")
    ProfilDTO updateProfil(@RequestBody ProfilDTO profilDTO) {
        return profilService.updateProfil(profilDTO);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "list")
    List<Profil> getProfils() {
        return profilRepository.findAll();
    }

    @GetMapping(path = "get/{id}")
    ProfilDTO getProfil(@PathVariable("id") String username) {
        return profilService.getProfil(username);
    }

    @GetMapping(path = "getPublicInformation")
    ProfilDTO getProfilInfo() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        return profilService.getPublicProfilInformation(username);
    }

    @DeleteMapping(path = "delete/{id}")
    ProfilDTO deleteProfil(@PathVariable("id") Long id) {
        return profilService.deleteProfil(id);
    }
}
