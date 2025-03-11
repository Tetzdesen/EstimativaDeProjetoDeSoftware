package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public interface IProjetoRepository {
    void salvar(Projeto projeto);

    void atualizar(Projeto projeto);
    
    void removerPorId(UUID id);

    Optional<Projeto> buscarPorId(UUID id);

    List<Projeto> buscarTodos();
    
    boolean buscarIsCompartilhadoPorId(UUID idUsuario, UUID idProjeto);
    
    List<String> buscarProjetosPorUsuario(UUID idUsuario);
}
