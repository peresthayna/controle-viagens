package com.agt.viagens.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Viagens Corporativas API")
                        .description("""
                                Backend do desafio técnico AGT — Controle de Viagens Corporativas.

                                **Endpoints prontos (scaffold):** /auth/**, /dominios/**

                                **Endpoints a implementar (candidato):** /viagens/**
                                """)
                        .version("1.0.0")
                        .contact(new Contact().name("Murilo Andrade — AGT").email("murilo.andrade@agt.com.br"))
                )
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Informe o accessToken obtido em POST /auth/login")
                        )
                );
    }
}
