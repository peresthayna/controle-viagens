package com.agt.viagens.presentation.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dominios")
@Tag(name = "Domínios", description = "Valores de domínio para preenchimento de formulários")
@SecurityRequirement(name = "bearerAuth")
public class DominioController {

    private static final List<String> FINALIDADES = List.of(
            "Visita Técnica", "Reunião", "Treinamento", "Entrega", "Outro"
    );

    private static final List<String> TRANSPORTES = List.of(
            "Carro Próprio", "Carro da Empresa", "Aéreo", "Ônibus"
    );

    @GetMapping("/finalidades")
    @Operation(summary = "Lista as finalidades disponíveis para registro de viagem")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<List<String>> finalidades() {
        return ResponseEntity.ok(FINALIDADES);
    }

    @GetMapping("/transportes")
    @Operation(summary = "Lista os meios de transporte disponíveis para registro de viagem")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<List<String>> transportes() {
        return ResponseEntity.ok(TRANSPORTES);
    }
}
