package com.br.estimativadeprojetodesoftware.repository;

import java.util.List;

/**
 *
 * @author tetzner
 */
public interface IUsuarioHasProjetoRepository {

    void salvar(String usuarioId, String projetoId, boolean isCompartilhado);

    void removerPorIds(String usuarioId, String projetoId);

    List<String> buscarProjetosPorUsuario(String usuarioId);
}
