package com.agt.viagens.application.dto.request;

import com.agt.viagens.domain.model.StatusViagem;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(
        @NotNull(message = "Status é obrigatório") StatusViagem status
) {}
