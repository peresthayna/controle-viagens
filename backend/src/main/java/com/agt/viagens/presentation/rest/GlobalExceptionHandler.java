package com.agt.viagens.presentation.rest;

import com.agt.viagens.application.dto.response.ErroResponse;
import com.agt.viagens.domain.exception.DataVoltaAnteriorADataIdaException;
import com.agt.viagens.domain.exception.TokenInvalidoException;
import com.agt.viagens.domain.exception.TransicaoStatusInvalidaException;
import com.agt.viagens.domain.exception.ViagemNaoEncontradaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── Domínio ────────────────────────────────────────────────────────────────

    @ExceptionHandler(ViagemNaoEncontradaException.class)
    public ResponseEntity<ErroResponse> handleViagemNaoEncontrada(ViagemNaoEncontradaException ex) {
        log.warn("Viagem não encontrada: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage());
    }

    @ExceptionHandler(TransicaoStatusInvalidaException.class)
    public ResponseEntity<ErroResponse> handleTransicaoInvalida(TransicaoStatusInvalidaException ex) {
        log.warn("Transição de status inválida: {}", ex.getMessage());
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "Transição de status inválida", ex.getMessage());
    }

    // ── Autenticação ───────────────────────────────────────────────────────────

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponse> handleBadCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciais inválidas", "Usuário ou senha incorretos.");
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<ErroResponse> handleTokenInvalido(TokenInvalidoException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Token inválido", ex.getMessage());
    }

    // ── Validação e requisição ─────────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, "Dados inválidos", mensagem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleNotReadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, "Requisição inválida",
                "O corpo da requisição está ausente ou malformado.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, "Parâmetro inválido",
                "O parâmetro '" + ex.getName() + "' recebeu um valor incompatível.");
    }

    // ── Stub do candidato ─────────────────────────────────────────────────────

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErroResponse> handleNotImplemented(UnsupportedOperationException ex) {
        return build(HttpStatus.NOT_IMPLEMENTED, "Não implementado",
                "Este endpoint ainda não foi implementado. Consulte ViagemServiceImpl.");
    }

    // ── Rota não encontrada ────────────────────────────────────────────────────

    @ExceptionHandler({
        org.springframework.web.servlet.resource.NoResourceFoundException.class,
        org.springframework.web.servlet.NoHandlerFoundException.class
    })
    public ResponseEntity<ErroResponse> handleNotFound(Exception ex) {
        return build(HttpStatus.NOT_FOUND, "Rota não encontrada", "O endpoint solicitado não existe.");
    }

    // ── Fallback ───────────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGeneric(Exception ex) {
        log.error("Erro interno não tratado", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno", "Ocorreu um erro inesperado.");
    }

    // ── Utilitário ─────────────────────────────────────────────────────────────

    private ResponseEntity<ErroResponse> build(HttpStatus status, String erro, String mensagem) {
        return ResponseEntity.status(status).body(
                new ErroResponse(status.value(), erro, mensagem, LocalDateTime.now(ZoneOffset.UTC))
        );
    }
    
    @ExceptionHandler(DataVoltaAnteriorADataIdaException.class)
    public ResponseEntity<ErroResponse> handleInvalidDates(DataVoltaAnteriorADataIdaException ex) {
        ErroResponse erro = new ErroResponse(
        		HttpStatus.UNPROCESSABLE_ENTITY.value(),
        		"Erro de data inválida",
        		ex.getMessage(),
        		LocalDateTime.now()
        );
        
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(erro);
    }
}
