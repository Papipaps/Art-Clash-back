package com.example.demo.service;

import com.example.demo.model.data.Profile;
import com.example.demo.model.data.Role;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    ProfileRepository profilRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void addRoleToUser(String username, String roleName) {
        Profile profil = profilRepository.findByUsername(username).get();
        Role role=roleRepository.findByName(roleName).get();
        profil.getRoles().add(role);
        profilRepository.save(profil);
    }

    @Override
    public Role saveRole(Role role) {
return roleRepository.save(role);
    }
}
