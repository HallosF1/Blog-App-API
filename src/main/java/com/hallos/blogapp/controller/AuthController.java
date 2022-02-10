package com.hallos.blogapp.controller;

import com.hallos.blogapp.dto.JWTAuthResponse;
import com.hallos.blogapp.dto.LoginDto;
import com.hallos.blogapp.dto.SignUpDto;
import com.hallos.blogapp.model.Role;
import com.hallos.blogapp.model.User;
import com.hallos.blogapp.repository.RoleRepository;
import com.hallos.blogapp.repository.UserRepository;
import com.hallos.blogapp.security.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenManager tokenManager;

    public AuthController(AuthenticationManager authenticationManager, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, TokenManager tokenManager) {
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenManager.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpDto signUpDto) {

        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();

        User user = User.builder()
                .username(signUpDto.getUsername())
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .roles(Collections.singleton(roles))
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("Signed up successfully");
    }
}
