package com.example.demo.controller;

import com.example.demo.model.data.Profile;
import com.example.demo.model.dto.ProfilDTO;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.service.ProfilService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/profil")
@Api(value = "Profile Controller")
 public class ProfilController {

    @Autowired
    ProfilService profilService;

    @Autowired
    ProfileRepository profilRepository;


    @GetMapping
    @ApiOperation(value = "Example endpoint", notes = "This endpoint is for demonstration purposes")
    public String hello(){
        return "hello";
    }
    @PutMapping(path = "update")
    ProfilDTO updateProfil(@RequestBody ProfilDTO profilDTO) {
        return profilService.updateProfil(profilDTO);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "list")
    List<Profile> getProfils() {
        return profilRepository.findAll();
    }

    //@GetMapping(path = "get/{id}")
    //ProfilDTO getProfil(@PathVariable("id") String username) {
    //    return profilService.getProfil(username);
    //}

    @GetMapping(path = "getProfilInformation")
    ProfilDTO getCurrentProfilInfo(@RequestParam(required = false) String username) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        username = username == null || username.isBlank() ? loggedInUser.getName() : username;
        return profilService.getProfil(username,true);
    }
    @GetMapping(path = "getProfil/{id}")
    ProfilDTO getProfil(@RequestParam(required = false) String id) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        return profilService.getProfil(id,false);
    }

    @GetMapping(path = "getPublicInformation")
    ProfilDTO getProfilInfo(@RequestParam(required = false) String username) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        username = username == null || username.isBlank() ? loggedInUser.getName() : username;
        return profilService.getPublicProfilInformation(username);
    }

    @DeleteMapping(path = "delete/{id}")
    ProfilDTO deleteProfil(@PathVariable("id") String id) {
        return profilService.deleteProfil(id);
    }
}
