package com.example.demo.service;

import com.example.demo.model.data.Profil;
import com.example.demo.model.data.Role;
import com.example.demo.repository.ProfilRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    ProfilRepository profilRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void addRoleToUser(String username, String roleName) {
        Profil profil = profilRepository.findByUsername(username).get();
        Role role=roleRepository.findByName(roleName).get();
        profil.getRoles().add(role);
        profilRepository.save(profil);
    }

    @Override
    public Role saveRole(Role role) {
return roleRepository.save(role);
    }
}
