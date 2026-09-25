package com.example.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            response.put("status", "Fail");
            response.put("message", "Email is already registered!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        response.put("status", "Success");
        response.put("message", "User registered successfully!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
