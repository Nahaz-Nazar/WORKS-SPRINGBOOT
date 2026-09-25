package com.example.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. REGISTER USER
    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            response.put("message", "Email already registered!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        response.put("message", "Registration successful!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 2. LOGIN USER (Generates Token)
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        Map<String, String> response = new HashMap<>();
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            // Generate a secure unique random token string
            String token = UUID.randomUUID().toString();
            tokenRepository.save(new AuthToken(token, email));
            
            response.put("message", "Login successful!");
            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        
        response.put("message", "Invalid email or password!");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 3. PROTECTED ROUTE (Requires Token via Header)
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        
        if (token == null || tokenRepository.findByToken(token).isEmpty()) {
            response.put("message", "Access Denied! Invalid or missing Token.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        AuthToken activeToken = tokenRepository.findByToken(token).get();
        User user = userRepository.findByEmail(activeToken.getUserEmail()).get();

        response.put("status", "Authorized Access Allowed");
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        response.put("mobileNumber", user.getMobileNumber());
        response.put("email", user.getEmail());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 4. LOGOUT ROUTE (Invalidates Token)
    @Transactional
    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, String> response = new HashMap<>();
        
        if (token != null && tokenRepository.findByToken(token).isPresent()) {
            tokenRepository.deleteByToken(token); // Permanently breaks the token
            response.put("message", "Logged out successfully. Token invalidated.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        
        response.put("message", "No active session found for this token.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
