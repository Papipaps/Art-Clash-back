 package com.example.demo.controller;


 import com.auth0.jwt.JWT;
 import com.auth0.jwt.JWTVerifier;
 import com.auth0.jwt.algorithms.Algorithm;
 import com.auth0.jwt.interfaces.DecodedJWT;
 import com.example.demo.model.data.Profil;
 import com.example.demo.model.data.Role;
 import com.example.demo.payload.request.LoginRequest;
 import com.example.demo.payload.request.SignupRequest;
 import com.example.demo.payload.response.MessageResponse;
 import com.example.demo.payload.response.UserInfoResponse;
 import com.example.demo.repository.ProfilRepository;
 import com.example.demo.security.services.UserDetailsImpl;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpHeaders;
 import org.springframework.http.MediaType;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.web.bind.annotation.*;

 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.validation.Valid;
 import java.time.Instant;
 import java.time.temporal.ChronoUnit;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.stream.Collectors;

 @CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    ProfilRepository profilRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;


     @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),roles));
    }




    @PostMapping("/signup")
    public ResponseEntity<?> registerUser( @RequestBody SignupRequest signUpRequest) {
        if (profilRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (profilRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Profil profil = new Profil();
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

        profilRepository.save(profil);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok()
                .body(new MessageResponse("You've been signed out!"));
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
                Profil profil = profilRepository.findByUsername(username).get();

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

