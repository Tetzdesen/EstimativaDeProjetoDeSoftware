package com.br.estimativadeprojetodesoftware.view.componentes;

/** Janela que pode indicar trabalho em andamento e bloquear as ações enquanto isso. */
public interface Ocupavel {

    /** @param mensagem texto curto do que está acontecendo (ex.: "Salvando…"); ignorado ao desligar */
    void setOcupado(boolean ocupado, String mensagem);

    boolean isOcupado();

    /** Janela já fechada: o resultado de uma operação não deve mais ser exibido nela. */
    boolean isFechada();
}
