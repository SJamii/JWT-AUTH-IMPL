package com.example.petProject.service;

import com.example.petProject.security.AuthRequest;
import com.example.petProject.security.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(AuthRequest request);
}
