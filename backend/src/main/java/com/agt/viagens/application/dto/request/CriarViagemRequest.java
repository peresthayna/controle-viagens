package com.agt.viagens.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CriarViagemRequest(
        @NotBlank(message = "Destino é obrigatório") String destino,
        @NotNull(message  = "Data de ida é obrigatória") LocalDate dataIda,
        @NotNull(message  = "Data de volta é obrigatória") LocalDate dataVolta,
        @NotBlank(message = "Finalidade é obrigatória") String finalidade,
        @NotBlank(message = "Transporte é obrigatório") String transporte,
        String observacoes
) {}
