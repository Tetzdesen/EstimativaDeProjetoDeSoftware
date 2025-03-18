package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public interface IPerfilRepository {

    void salvar(PerfilProjeto Perfil);

    void atualizar(PerfilProjeto Perfil);

    void removerPorId(UUID id);

    Optional<PerfilProjeto> buscarPorId(UUID id);

    List<PerfilProjeto> buscarPerfisPorProjeto(UUID idProjeto);

    Map<String, Double> buscarTodosCamposPorPerfil(PerfilProjeto perfil);

    List<PerfilProjeto> buscarTodos();

    List<PerfilProjeto> buscarTodosPerfisPorIdUsuario(UUID id);
    
    int obterQuantidadePerfisPorUsuario(UUID idUsuario);
}
