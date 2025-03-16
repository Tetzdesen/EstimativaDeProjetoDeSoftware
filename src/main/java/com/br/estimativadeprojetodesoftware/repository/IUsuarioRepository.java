package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Usuario;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public interface IUsuarioRepository {

    void salvar(Usuario usuario);

    void atualizar(Usuario usuario);
    
    boolean removerPorId(UUID id);

    Optional<Usuario> buscarPorId(UUID id);
    
    Optional<Usuario> buscarPorEmail(String email);
    
    List<Usuario> buscarTodos();
    
    Map<String, String> buscarEmailESenhaDeUsuarios();
    
    List<Usuario> buscarUsuariosPorProjeto(UUID idProjeto);
}
