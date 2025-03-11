package com.br.estimativadeprojetodesoftware.repository;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public interface IUsuarioHasProjetoRepository {

    void salvar(UUID idUsuario, UUID idProjeto, boolean isCompartilhado);

    void removerPorIds(UUID idUsuario, UUID idProjeto);

    boolean buscarIsCompartilhadoPorId(UUID idUsuario, UUID idProjeto);
    
    List<String> buscarProjetosPorUsuario(UUID idUsuario);
}
