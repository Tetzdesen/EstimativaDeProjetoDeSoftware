package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Estimativa;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public interface IEstimativaRepository {
    void salvar(Estimativa estimativa);

    void atualizar(Estimativa estimativa);
    
    void removerPorId(UUID id);

    Optional<Estimativa> buscarPorId(UUID id);

    List<Estimativa> buscarTodos();
}
