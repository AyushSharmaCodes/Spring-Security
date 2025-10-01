package com.example.springsecurity.controller;

import com.example.springsecurity.entity.User;
import com.example.springsecurity.repository.UserRepo;
import com.example.springsecurity.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor(onConstructor_ =  @Autowired)
public class UserController {

    private JwtUtil jwtUtil;

    private final UserRepo userRepo;

    private final PasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Create authentication token with email (username) and password
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

            // Authenticate the user
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            if (authenticate.isAuthenticated()) {
                // Get the authenticated user details
                UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

                // Generate JWT token
                String jwtToken = jwtUtil.generateToken(userDetails);

                // Return token in a clean JSON response
                Map<String, String> response = new HashMap<>();
                response.put("token", jwtToken);
                response.put("message", "Login successful");

                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid user credentials!", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Authentication failed: " + e.getMessage(),
                                       HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User u = userRepo.save(user);
        if(u.getId() != null){
            return new ResponseEntity<>("User registered successfully! with user id "+ u.getId(), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>("User registration failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the JWT Authentication Demo!";
    }
}
