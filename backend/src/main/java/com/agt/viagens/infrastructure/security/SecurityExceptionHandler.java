package com.agt.viagens.infrastructure.security;

import com.agt.viagens.application.dto.response.ErroResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Garante que falhas de autenticação e autorização retornem JSON no formato
 * padronizado {@link ErroResponse}, em vez do corpo vazio padrão do Spring Security.
 */
@Component
@RequiredArgsConstructor
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        escrever(response, HttpStatus.UNAUTHORIZED,
                "Não autenticado", "Token ausente, inválido ou expirado.");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {
        escrever(response, HttpStatus.FORBIDDEN,
                "Acesso negado", "Você não tem permissão para acessar este recurso.");
    }

    private void escrever(HttpServletResponse response, HttpStatus status,
                          String erro, String mensagem) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(),
                new ErroResponse(status.value(), erro, mensagem, LocalDateTime.now(ZoneOffset.UTC)));
    }
}
