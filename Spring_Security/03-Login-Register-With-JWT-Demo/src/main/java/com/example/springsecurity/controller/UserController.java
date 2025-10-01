package com.example.springsecurity.controller;

import com.example.springsecurity.entity.User;
import com.example.springsecurity.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor_ =  @Autowired)
public class UserController {

    private final UserRepo userRepo;

    private final PasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);
        if(authenticate.isAuthenticated()){
            return new ResponseEntity<>("User logged in successfully!", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Invalid user credentials!", HttpStatus.UNAUTHORIZED);
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
}
