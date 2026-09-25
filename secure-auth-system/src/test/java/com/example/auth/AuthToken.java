package com.example.auth;

import jakarta.persistence.*;

@Entity
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String userEmail;

    public AuthToken() {}

    public AuthToken(String token, String userEmail) {
        this.token = token;
        this.userEmail = userEmail;
    }

    public Long getId() { return id; }
    public String getToken() { return token; }
    public String getUserEmail() { return userEmail; }
}
