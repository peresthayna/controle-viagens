package com.agt.viagens.infrastructure.persistence;

import com.agt.viagens.domain.model.Usuario;
import com.agt.viagens.domain.model.Viagem;
import com.agt.viagens.domain.port.ViagemPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class ViagemPortImpl implements ViagemPort {

    private final ViagemRepositoryJpa repository;

    @Override
    public List<Viagem> buscarPorUsuario(Usuario usuario) {
        return repository.findByUsuarioOrderByCriadoEmDesc(usuario);
    }

    @Override
    public Optional<Viagem> buscarPorIdEUsuario(Long id, Usuario usuario) {
        return repository.findByIdAndUsuario(id, usuario);
    }

    @Override
    public Viagem salvar(Viagem viagem) {
        return repository.save(viagem);
    }
}
