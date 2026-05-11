package com.agt.viagens.application.service;

import com.agt.viagens.application.dto.request.AtualizarStatusRequest;
import com.agt.viagens.application.dto.request.CriarViagemRequest;
import com.agt.viagens.application.dto.response.ViagemResponse;
import com.agt.viagens.domain.model.Usuario;

import java.util.List;

/**
 * Casos de uso de gerenciamento de viagens corporativas.
 * Implemente esta interface em {@link ViagemServiceImpl}.
 */
public interface ViagemService {

    /**
     * Retorna todas as viagens do usuário autenticado, ordenadas por data de criação (mais recentes primeiro).
     *
     * @param usuario usuário autenticado (obtido do SecurityContext via @AuthenticationPrincipal)
     * @return lista de viagens do usuário
     */
    List<ViagemResponse> listarPorUsuario(Usuario usuario);

    /**
     * Cria uma nova viagem com status inicial {@code AGENDADA} e a associa ao usuário autenticado.
     *
     * @param request dados da nova viagem — campos obrigatórios validados via Bean Validation
     * @param usuario usuário autenticado
     * @return viagem criada com id e timestamps preenchidos
     */
    ViagemResponse criar(CriarViagemRequest request, Usuario usuario);

    /**
     * Atualiza o status de uma viagem respeitando a máquina de estados definida em {@link com.agt.viagens.domain.model.StatusViagem}.
     *
     * <p>Regras de transição válidas:
     * <ul>
     *   <li>AGENDADA → EM_ANDAMENTO</li>
     *   <li>AGENDADA → CANCELADA</li>
     *   <li>EM_ANDAMENTO → CONCLUIDA</li>
     *   <li>EM_ANDAMENTO → CANCELADA</li>
     * </ul>
     *
     * @param id      identificador da viagem
     * @param request novo status desejado
     * @param usuario usuário autenticado — a viagem deve pertencer a ele
     * @return viagem com status atualizado
     * @throws com.agt.viagens.domain.exception.ViagemNaoEncontradaException   se a viagem não existir ou não pertencer ao usuário
     * @throws com.agt.viagens.domain.exception.TransicaoStatusInvalidaException se a transição solicitada não for permitida
     */
    ViagemResponse atualizarStatus(Long id, AtualizarStatusRequest request, Usuario usuario);
}
