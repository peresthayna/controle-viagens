package com.agt.viagens.application.mapper;

import com.agt.viagens.application.dto.response.ViagemResponse;
import com.agt.viagens.domain.model.Viagem;
import org.springframework.stereotype.Component;

@Component
public class ViagemMapper {

    public ViagemResponse toResponse(Viagem viagem) {
        return new ViagemResponse(
                viagem.getId(),
                viagem.getDestino(),
                viagem.getDataIda(),
                viagem.getDataVolta(),
                viagem.getFinalidade(),
                viagem.getTransporte(),
                viagem.getObservacoes(),
                viagem.getStatus(),
                viagem.getCriadoEm()
        );
    }
}
