package com.agt.viagens.application.service;

import com.agt.viagens.application.dto.request.LoginRequest;
import com.agt.viagens.application.dto.request.RefreshTokenRequest;
import com.agt.viagens.application.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RefreshTokenRequest request);
}
