package com.agt.viagens.domain.port;

import com.agt.viagens.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioPort {
    Optional<Usuario> buscarPorUsername(String username);
    Usuario salvar(Usuario usuario);
}
