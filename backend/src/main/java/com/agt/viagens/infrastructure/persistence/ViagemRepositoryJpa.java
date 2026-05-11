package com.agt.viagens.infrastructure.persistence;

import com.agt.viagens.domain.model.Usuario;
import com.agt.viagens.domain.model.Viagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface ViagemRepositoryJpa extends JpaRepository<Viagem, Long> {
    List<Viagem> findByUsuarioOrderByCriadoEmDesc(Usuario usuario);
    Optional<Viagem> findByIdAndUsuario(Long id, Usuario usuario);
}
