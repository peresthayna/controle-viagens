package com.agt.viagens.infrastructure.persistence;

import com.agt.viagens.domain.model.Usuario;
import com.agt.viagens.domain.port.UsuarioPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class UsuarioPortImpl implements UsuarioPort {

    private final UsuarioRepositoryJpa repository;

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        return repository.save(usuario);
    }
}
