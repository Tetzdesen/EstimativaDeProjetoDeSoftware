package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public interface IProjetoRepository {
    void salvar(Projeto projeto);

    void salvar(Projeto projeto, Usuario usuario);

    void atualizar(Projeto projeto);
    
    void removerPorId(UUID id);

    Optional<Projeto> buscarPorId(UUID id);
    
    Optional<Projeto> buscarProjetoPorNome(String nomeProjeto);

    List<Projeto> buscarTodos();
    
    boolean buscarIsCompartilhadoPorId(UUID idUsuario, UUID idProjeto);
       
    List<String> buscarNomesDeProjetosPorUsuario(UUID idUsuario);

    List<String> buscarNomesDeProjetosCompartilhadosPorUsuario(UUID idUsuario);
    
    List<String> buscarProjetosPorUsuario(UUID idUsuario);
}
