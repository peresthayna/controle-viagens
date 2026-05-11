package com.agt.viagens.application.service;

import com.agt.viagens.application.dto.request.LoginRequest;
import com.agt.viagens.application.dto.request.RefreshTokenRequest;
import com.agt.viagens.application.dto.response.AuthResponse;
import com.agt.viagens.config.JwtProperties;
import com.agt.viagens.domain.exception.TokenInvalidoException;
import com.agt.viagens.domain.model.RefreshToken;
import com.agt.viagens.domain.model.Usuario;
import com.agt.viagens.domain.port.RefreshTokenPort;
import com.agt.viagens.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenPort refreshTokenPort;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        Usuario usuario = (Usuario) authentication.getPrincipal();

        refreshTokenPort.revogarTodosDoUsuario(usuario);

        String accessToken   = jwtService.gerarToken(usuario);
        RefreshToken refresh = criarRefreshToken(usuario);

        log.info("Login realizado — usuário: {}", usuario.getUsername());
        return new AuthResponse("Bearer", accessToken, refresh.getToken(), jwtProperties.expiration());
    }

    @Override
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenPort.buscarPorToken(request.refreshToken())
                .orElseThrow(() -> new TokenInvalidoException("Refresh token inválido."));

        if (refreshToken.isRevogado() || refreshToken.getExpiresAt().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            throw new TokenInvalidoException("Refresh token expirado ou revogado.");
        }

        refreshTokenPort.revogarTodosDoUsuario(refreshToken.getUsuario());

        String novoAccessToken  = jwtService.gerarToken(refreshToken.getUsuario());
        RefreshToken novoRefresh = criarRefreshToken(refreshToken.getUsuario());

        log.info("Token renovado — usuário: {}", refreshToken.getUsuario().getUsername());
        return new AuthResponse("Bearer", novoAccessToken, novoRefresh.getToken(), jwtProperties.expiration());
    }

    private RefreshToken criarRefreshToken(Usuario usuario) {
        return refreshTokenPort.salvar(
                RefreshToken.builder()
                        .token(UUID.randomUUID().toString())
                        .usuario(usuario)
                        .expiresAt(LocalDateTime.now(ZoneOffset.UTC).plusSeconds(jwtProperties.refreshExpiration()))
                        .revogado(false)
                        .build()
        );
    }
}
