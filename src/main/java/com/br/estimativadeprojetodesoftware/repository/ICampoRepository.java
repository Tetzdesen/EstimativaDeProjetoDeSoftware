package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public interface ICampoRepository {

    void salvar(Campo campo);

    void salvarProjetoCampo(Projeto projeto, Campo campo);

    void salvarPerfilCampo(PerfilProjeto perfil, Campo campo);

    void salvarPerfilCampos(PerfilProjeto perfil, List<Campo> campos);

    void atualizar(Campo campo);

    void atualizarDiasProjetoCampo(Projeto projeto, Campo campo);

    void atualizarDiasPerfilCampo(PerfilProjeto perfil, Campo campo);

    void removerPorID(UUID id);

    void removerPorIdPerfil(UUID idPerfil);

    Campo buscarPorId(UUID id);

    Integer buscarDiasPorProjetoCampo(UUID idProjeto, UUID idCampo);

    Double buscarDiasPorPerfilCampo(UUID idPerfil, UUID idCampo);

    boolean isCampoInPerfil(UUID idPerfil, UUID idCampo);

    Campo buscarPorNome(String nome);

    Integer buscarDiasPorProjeto(UUID idProjeto);

    Double buscarDiasPorPerfil(UUID idPerfil);

    Campo buscarPorIdProjeto(UUID idProjeto);

    Campo buscarPorIdPerfil(UUID idPerfil);

    List<Campo> buscarPorIdProjetoTipo(UUID idProjeto, String tipo);

    List<Campo> buscarPorIdPerfilTipo(UUID idPerfil, String tipo);

    List<Campo> listarTodos();

    List<Campo> listarTodosPorTipo(String tipo);

    List<Campo> listarTodosPorIdProjeto(UUID idProjeto);

    List<Campo> listarTodosPorIdPerfil(UUID idPerfil);

    double buscarValorPorNomeProjetoCampo(UUID idProjeto, String nome);
}
