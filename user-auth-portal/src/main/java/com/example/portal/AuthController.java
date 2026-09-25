package com.example.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

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

    // 1. USER REGISTRATION ENDPOINT
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        // Validation A: All fields must be filled
        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
            user.getEmail() == null || user.getEmail().trim().isEmpty() ||
            user.getPassword() == null || user.getPassword().trim().isEmpty() ||
            user.getConfirmPassword() == null || user.getConfirmPassword().trim().isEmpty()) {
            
            response.put("error", "Registration Failed");
            response.put("message", "All fields must be filled!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Validation B: Password and ConfirmPassword must match
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            response.put("error", "Registration Failed");
            response.put("message", "Password and Confirm Password do not match!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Validation C: Email must not already exist in the database
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            response.put("error", "Registration Failed");
            response.put("message", "Email already exists in the database!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Hashing the password using BCrypt before archiving to DB
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        response.put("status", "Success");
        response.put("message", "User registered successfully!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 2. USER LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> credentials) {
        Map<String, String> response = new HashMap<>();
        String email = credentials.get("email");
        String password = credentials.get("password");

        // Validate basic payload entry existence
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.put("error", "Login Failed");
            response.put("message", "Email and password fields are required!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<User> userOpt = userRepository.findByEmail(email);

        // Verification validation tracking matching constraints
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            User user = userOpt.get();
            
            // Generate random unique security token
            String generatedToken = UUID.randomUUID().toString();
            
            // Save the generated token directly into the user table record
            user.setToken(generatedToken);
            userRepository.save(user);

            response.put("message", "Login successful!");
            response.put("token", generatedToken);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // Mismatched authentication credentials error fallback handling
        response.put("error", "Unauthorized");
        response.put("message", "Incorrect email or password!");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 3. USER LOGOUT ENDPOINT (Only users with a valid token can logout)
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, String> response = new HashMap<>();

        if (token == null || token.trim().isEmpty()) {
            response.put("error", "Access Denied");
            response.put("message", "Missing token parameter in the Authorization header!");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        // Check if the provided token exists in the user table records
        Optional<User> userOpt = userRepository.findByToken(token);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Invalidate token by resetting the value to null
            user.setToken(null);
            userRepository.save(user);

            response.put("status", "Success");
            response.put("message", "Logged out successfully. Token invalidated from user record.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // Catch clause fallback when an invalid, mismatched token is passed in header
        response.put("error", "Forbidden");
        response.put("message", "Invalid token string supplied! Logout operation rejected.");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
