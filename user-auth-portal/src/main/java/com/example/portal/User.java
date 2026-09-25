package com.example.portal;

import jakarta.persistence.*;

@Entity
@Table(name = "portal_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    
    @Transient // Not saved in database table, used strictly for validation comparison
    private String confirmPassword;
    
    private String token; // Store token directly in the user table upon login

    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
