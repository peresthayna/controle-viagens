package com.agt.viagens.infrastructure.persistence;

import com.agt.viagens.domain.model.RefreshToken;
import com.agt.viagens.domain.model.Usuario;
import com.agt.viagens.domain.port.RefreshTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class RefreshTokenPortImpl implements RefreshTokenPort {

    private final RefreshTokenRepositoryJpa repository;

    @Override
    public RefreshToken salvar(RefreshToken refreshToken) {
        return repository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> buscarPorToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public void revogarTodosDoUsuario(Usuario usuario) {
        repository.revogarTodosPorUsuario(usuario);
    }
}
