package com.agt.viagens.domain.model;

/**
 * Ciclo de vida de uma viagem corporativa.
 * As regras de transição são a única fonte de verdade — use podeTransicionarPara() antes de qualquer alteração de status.
 */
public enum StatusViagem {

    AGENDADA,
    EM_ANDAMENTO,
    CONCLUIDA,
    CANCELADA;

    /**
     * Valida se a transição do status atual para o status de destino é permitida.
     *
     * <pre>
     * AGENDADA     → EM_ANDAMENTO, CANCELADA
     * EM_ANDAMENTO → CONCLUIDA, CANCELADA
     * CONCLUIDA    → (nenhuma — status terminal)
     * CANCELADA    → (nenhuma — status terminal)
     * </pre>
     *
     * @param destino status para o qual se deseja transicionar
     * @return {@code true} se a transição for válida
     */
    public boolean podeTransicionarPara(StatusViagem destino) {
        return switch (this) {
            case AGENDADA     -> destino == EM_ANDAMENTO || destino == CANCELADA;
            case EM_ANDAMENTO -> destino == CONCLUIDA    || destino == CANCELADA;
            default           -> false;
        };
    }
}
