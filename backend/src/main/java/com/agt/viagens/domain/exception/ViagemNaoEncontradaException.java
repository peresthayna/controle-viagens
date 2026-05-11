package com.agt.viagens.domain.exception;

public class ViagemNaoEncontradaException extends RuntimeException {

    public ViagemNaoEncontradaException(Long id) {
        super("Viagem com id " + id + " não encontrada ou não pertence ao usuário autenticado.");
    }
}
