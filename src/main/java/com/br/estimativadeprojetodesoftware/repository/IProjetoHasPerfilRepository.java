package com.br.estimativadeprojetodesoftware.repository;

/**
 *
 * @author tetzner
 */

import java.util.List;

public interface IProjetoHasPerfilRepository {

    void salvar(String projetoId, String perfilId);

    void removerPorIds(String projetoId, String perfilId);

    List<String> buscarPerfisPorProjeto(String projetoId);

    List<String> buscarProjetosPorPerfil(String perfilId);
}
