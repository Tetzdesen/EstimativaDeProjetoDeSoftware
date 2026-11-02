package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPerfilRepository {

    /** Insere ou substitui o perfil inteiro (funcionalidades, valores e taxas). */
    void salvar(Perfil perfil);

    boolean remover(UUID id);

    Optional<Perfil> buscarPorId(UUID id);

    /** Perfis padrão e perfis do usuário. */
    List<Perfil> buscarVisiveisPara(UUID usuarioId);

    boolean existePerfilPadrao();
}
