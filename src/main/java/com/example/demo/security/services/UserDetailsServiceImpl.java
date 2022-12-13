package com.example.demo.security.services;

import com.example.demo.model.data.Profile;
import com.example.demo.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  ProfileRepository profilRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Profile profil = profilRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
      Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
      profil.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
    return new User(profil.getUsername(),profil.getPassword(),authorities);
  }

}


