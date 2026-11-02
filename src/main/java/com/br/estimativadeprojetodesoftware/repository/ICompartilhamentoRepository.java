package com.br.estimativadeprojetodesoftware.repository;

import java.util.List;
import java.util.UUID;

public interface ICompartilhamentoRepository {

    void compartilhar(UUID projetoId, UUID usuarioId);

    boolean estaCompartilhado(UUID projetoId, UUID usuarioId);

    /** Ids dos projetos compartilhados com o usuário. */
    List<UUID> buscarProjetosCompartilhadosCom(UUID usuarioId);

    /** Ids dos usuários com quem o projeto foi compartilhado. */
    List<UUID> buscarDestinatarios(UUID projetoId);
}
