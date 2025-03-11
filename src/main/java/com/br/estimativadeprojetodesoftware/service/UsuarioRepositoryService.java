package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class UsuarioRepositoryService {

    private static UsuarioRepositoryService INSTANCIA = null;
    private final FabricaRepository fabricaDAO;
    private final IUsuarioRepository usuarioRepository;

    private UsuarioRepositoryService() {
        fabricaDAO = SeletorFabricaRepository.obterInstancia();
        usuarioRepository = fabricaDAO.criarUsuarioRepository();
    }

    public static UsuarioRepositoryService getInstancia() {
        if(INSTANCIA == null){
            INSTANCIA = new UsuarioRepositoryService();
        }
        
        return INSTANCIA;
    }

    public void salvar(Usuario usuario) {
        usuarioRepository.salvar(usuario);
    }

    public void atualizar(Usuario usuario) {
        usuarioRepository.atualizar(usuario);
    }

    public void removerPorId(UUID id) {
        usuarioRepository.removerPorId(id);
    }

    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.buscarPorId(id);
    }

    public List<Usuario> buscarTodos() {
        return usuarioRepository.buscarTodos();
    }
    
    public List<Usuario> buscarUsuariosPorProjeto(UUID idProjeto) {
        return usuarioRepository.buscarUsuariosPorProjeto(idProjeto);
    }
}
