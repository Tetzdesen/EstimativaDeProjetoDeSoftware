package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public interface ICampoRepository {

    void salvar(Campo campo);
    
    void atualizar(Campo campo);
    
    void atualizarDiasProjetoCampo(Projeto projeto, Campo campo);

    void atualizarDiasPerfilCampo(Perfil perfil, Campo campo);
    
    void removerPorID(UUID id);

    Campo buscarPorId(UUID id);

    List<Campo> listarTodos();
    
    Integer buscarDiasPorProjetoCampo(UUID idProjeto, UUID idCampo);
    
    Double buscarDiasPorPerfilCampo(UUID idPerfil, UUID idCampo);
    
    Integer buscarDiasPorProjeto(UUID idProjeto);
    Double buscarDiasPorPerfil(UUID idPerfil);
   // List<Campo> listarTodosPorIdProjetoCampo(UUID idProjeto, UUID idCampo);
    List<Campo> listarTodosPorIdProjeto(UUID idProjeto);
    List<Campo> listarTodosPorIdPerfil(UUID idPerfil);
    Campo buscarPorIdProjeto(UUID idProjeto);
    Campo buscarPorIdPerfil(UUID idPerfil);
    Campo buscarPorIdProjetoTipo(UUID idProjeto, String tipo);
    List<Campo> buscarPorIdPerfilTipo(UUID idPerfil, String tipo);
}
