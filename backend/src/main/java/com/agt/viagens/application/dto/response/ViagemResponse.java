package com.agt.viagens.application.dto.response;

import com.agt.viagens.domain.model.StatusViagem;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ViagemResponse(
        Long id,
        String destino,
        LocalDate dataIda,
        LocalDate dataVolta,
        String finalidade,
        String transporte,
        String observacoes,
        StatusViagem status,
        LocalDateTime criadoEm
) {}
