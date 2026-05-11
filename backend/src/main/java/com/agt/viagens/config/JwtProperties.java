package com.agt.viagens.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Propriedades JWT lidas do application.properties (prefixo "jwt").
 * Validadas no startup — falha rápida se a configuração estiver ausente ou inválida.
 */
@ConfigurationProperties(prefix = "jwt")
@Validated
public record JwtProperties(

        @NotBlank(message = "jwt.secret é obrigatório")
        String secret,

        @Positive(message = "jwt.expiration deve ser positivo")
        long expiration,

        @Positive(message = "jwt.refresh-expiration deve ser positivo")
        long refreshExpiration
) {}
