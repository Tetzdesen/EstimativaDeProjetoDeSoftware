package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class PerfilRepositoryService {

    private static PerfilRepositoryService INSTANCIA = null;
    private final FabricaRepository fabricaDAO;
    private final IPerfilRepository perfilRepository;

    private PerfilRepositoryService() {
        fabricaDAO = SeletorFabricaRepository.obterInstancia();
        perfilRepository = fabricaDAO.criarPerfilRepository();
    }

    public static PerfilRepositoryService getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new PerfilRepositoryService();
        }

        return INSTANCIA;
    }

    public void salvar(Perfil perfil){
        perfilRepository.salvar(perfil);
    }

    public void atualizar(Perfil perfil){
        perfilRepository.atualizar(perfil);
    }

    public void removerPorId(UUID id){
        perfilRepository.removerPorId(id);
    }

    public Optional<Perfil> buscarPorId(UUID id){
        return perfilRepository.buscarPorId(id);
    }
    
    public List<Perfil> buscarTodosPerfisPorIdUsuario(UUID id){
        return perfilRepository.buscarTodosPerfisPorIdUsuario(id);
    }

    public List<Perfil> buscarTodos(){
      return perfilRepository.buscarTodos();
    }
}
