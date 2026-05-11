package com.agt.viagens.domain.port;

import com.agt.viagens.domain.model.RefreshToken;
import com.agt.viagens.domain.model.Usuario;

import java.util.Optional;

public interface RefreshTokenPort {
    RefreshToken salvar(RefreshToken refreshToken);
    Optional<RefreshToken> buscarPorToken(String token);
    void revogarTodosDoUsuario(Usuario usuario);
}
