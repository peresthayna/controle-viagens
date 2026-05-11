package com.agt.viagens.presentation.rest;

import com.agt.viagens.application.dto.request.AtualizarStatusRequest;
import com.agt.viagens.application.dto.request.CriarViagemRequest;
import com.agt.viagens.application.dto.response.ViagemResponse;
import com.agt.viagens.application.service.ViagemService;
import com.agt.viagens.domain.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * ============================================================
 *  CANDIDATO — endpoints já mapeados. Implemente em ViagemServiceImpl.
 * ============================================================
 */
@RestController
@RequestMapping("/viagens")
@RequiredArgsConstructor
@Tag(name = "Viagens", description = "Gerenciamento de viagens corporativas")
@SecurityRequirement(name = "bearerAuth")
public class ViagemController {

    private final ViagemService viagemService;

    @GetMapping
    @Operation(summary = "Lista todas as viagens do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<List<ViagemResponse>> listar(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(viagemService.listarPorUsuario(usuario));
    }

    @PostMapping
    @Operation(summary = "Cria uma nova viagem com status inicial AGENDADA")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Viagem criada — header Location aponta para o recurso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<ViagemResponse> criar(
            @Valid @RequestBody CriarViagemRequest request,
            @AuthenticationPrincipal Usuario usuario,
            UriComponentsBuilder ucb
    ) {
        ViagemResponse criada = viagemService.criar(request, usuario);
        URI location = ucb.path("/viagens/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status de uma viagem respeitando as regras de transição")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Viagem não encontrada"),
            @ApiResponse(responseCode = "422", description = "Transição de status inválida")
    })
    public ResponseEntity<ViagemResponse> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusRequest request,
            @AuthenticationPrincipal Usuario usuario
    ) {
        return ResponseEntity.ok(viagemService.atualizarStatus(id, request, usuario));
    }
}
