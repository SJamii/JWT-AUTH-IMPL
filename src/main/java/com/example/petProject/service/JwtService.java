package com.example.petProject.service;

import com.example.petProject.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(User userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateRefreshToken(User userDetails);
}
