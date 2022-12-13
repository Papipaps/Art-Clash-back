 package com.example.demo.controller;


 import com.auth0.jwt.JWT;
 import com.auth0.jwt.JWTVerifier;
 import com.auth0.jwt.algorithms.Algorithm;
 import com.auth0.jwt.interfaces.DecodedJWT;
 import com.example.demo.model.data.ERole;
 import com.example.demo.model.data.Profile;
 import com.example.demo.model.data.Role;
 import com.example.demo.model.dto.ProfilDTO;
 import com.example.demo.payload.request.SignupRequest;
 import com.example.demo.repository.ProfileRepository;
 import com.example.demo.service.RoleService;
 import com.example.demo.utils.mapper.ProfilMapper;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpHeaders;
 import org.springframework.http.MediaType;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.web.bind.annotation.*;

 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import java.time.Instant;
 import java.time.temporal.ChronoUnit;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.stream.Collectors;

 @CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    ProfileRepository profilRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
     private ProfilMapper profilMapper;

    @Autowired
    private RoleService roleService;


    @PostMapping("/signup")
    public ProfilDTO registerUser(@RequestBody SignupRequest signUpRequest) {
        if (profilRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UsernameNotFoundException("Username already exists");
        }

        if (profilRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UsernameNotFoundException("Email already exists");
        }

        // Create new user's account
        Profile profil = new Profile();

        if (signUpRequest.getUsername() != null) {
            profil.setUsername(signUpRequest.getUsername());
        }
        if (signUpRequest.getFirstname() != null) {
            profil.setFirstname(signUpRequest.getFirstname());
        }
        if (signUpRequest.getLastname() != null) {
            profil.setLastname(signUpRequest.getLastname());
        }
        if (signUpRequest.getGender()!=null){
            profil.setGender(signUpRequest.getGender());
        }
        if (signUpRequest.getEmail() != null) {
            profil.setEmail(signUpRequest.getEmail());
        }
        if (signUpRequest.getCategory() != null) {
            profil.setCategory(signUpRequest.getCategory());
        }
        if (signUpRequest.getPassword() != null) {
            profil.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        }
        Profile save = profilRepository.save(profil);
        roleService.addRoleToUser(save.getUsername(), ERole.ROLE_USER.name());

        return profilMapper.profilEntityToDTO(save);

     }
    @PostMapping("/token/refresh")
     public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Profile profil = profilRepository.findByUsername(username).get();

                String access_token = JWT.create()
                        .withSubject(profil.getUsername())
                        .withExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",profil.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String,String> tokens= new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            throw new RuntimeException("Refresh token is missing !");
        }
    }
}

