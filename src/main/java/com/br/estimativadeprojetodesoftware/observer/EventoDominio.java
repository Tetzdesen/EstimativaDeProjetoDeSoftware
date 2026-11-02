package com.br.estimativadeprojetodesoftware.observer;

import java.util.UUID;

/** Evento publicado quando um dado muda: tipo da mudança, entidade e id. */
public record EventoDominio(Tipo tipo, Entidade entidade, UUID id) {

    public enum Tipo {
        CRIACAO, ALTERACAO, EXCLUSAO
    }

    public enum Entidade {
        USUARIO, PERFIL, PROJETO, COMPARTILHAMENTO, SESSAO, TEMA
    }

    public boolean sobre(Entidade outra) {
        return entidade == outra;
    }
}
