package com.agt.viagens.application.dto.response;

public record AuthResponse(
        String tokenType,
        String accessToken,
        String refreshToken,
        long expiresIn
) {}
