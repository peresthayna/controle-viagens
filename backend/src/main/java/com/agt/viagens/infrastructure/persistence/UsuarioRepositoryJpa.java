package com.agt.viagens.infrastructure.persistence;

import com.agt.viagens.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UsuarioRepositoryJpa extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
