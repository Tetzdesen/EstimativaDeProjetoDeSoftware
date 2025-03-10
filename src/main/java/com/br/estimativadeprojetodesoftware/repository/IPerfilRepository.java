package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public interface IPerfilRepository {

    void salvar(Perfil Perfil);

    void atualizar(Perfil Perfil);

    void removerPorId(UUID id);

    Optional<Perfil> buscarPorId(UUID id);

    List<Perfil> buscarTodos();
}
