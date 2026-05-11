package com.agt.viagens.infrastructure.persistence;

import com.agt.viagens.domain.model.RefreshToken;
import com.agt.viagens.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

interface RefreshTokenRepositoryJpa extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revogado = true WHERE rt.usuario = :usuario")
    void revogarTodosPorUsuario(Usuario usuario);
}
