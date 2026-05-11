package com.agt.viagens.domain.port;

import com.agt.viagens.domain.model.Usuario;
import com.agt.viagens.domain.model.Viagem;

import java.util.List;
import java.util.Optional;

public interface ViagemPort {
    List<Viagem> buscarPorUsuario(Usuario usuario);
    Optional<Viagem> buscarPorIdEUsuario(Long id, Usuario usuario);
    Viagem salvar(Viagem viagem);
}
