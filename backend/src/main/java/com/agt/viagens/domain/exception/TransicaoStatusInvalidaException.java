package com.agt.viagens.domain.exception;

import com.agt.viagens.domain.model.StatusViagem;

public class TransicaoStatusInvalidaException extends RuntimeException {

    public TransicaoStatusInvalidaException(StatusViagem atual, StatusViagem destino) {
        super("Transição de '" + atual.name() + "' para '" + destino.name() + "' não é permitida.");
    }
}
