package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioHasProjetoRepository;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class UsuarioHasProjetoRepositoryService {

    private static UsuarioHasProjetoRepositoryService INSTANCIA = null;
    private final FabricaRepository fabricaDAO;
    private final IUsuarioHasProjetoRepository usuarioProjetoRepository;

    private UsuarioHasProjetoRepositoryService() {
        fabricaDAO = SeletorFabricaRepository.obterInstancia();
        usuarioProjetoRepository = fabricaDAO.criarUsuarioProjetoRepository();
    }

    public static UsuarioHasProjetoRepositoryService getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new UsuarioHasProjetoRepositoryService();
        }

        return INSTANCIA;
    }

    public void salvar(UUID idUsuario, UUID idProjeto, boolean isCompartilhado) {
        usuarioProjetoRepository.salvar(idUsuario, idProjeto, isCompartilhado);
    }

    public void removerPorIds(UUID idUsuario, UUID idProjeto) {
        usuarioProjetoRepository.removerPorIds(idUsuario, idProjeto);
    }
    
    public boolean buscarIsCompartilhadoPorId(UUID idUsuario, UUID idProjeto){
        return usuarioProjetoRepository.buscarIsCompartilhadoPorId(idUsuario, idProjeto);
    }

    public List<String> buscarProjetosPorUsuario(UUID idUsuario) {
        return usuarioProjetoRepository.buscarProjetosPorUsuario(idUsuario);
    }

}
