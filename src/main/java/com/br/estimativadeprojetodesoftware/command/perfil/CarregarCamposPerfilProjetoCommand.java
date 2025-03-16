package com.br.estimativadeprojetodesoftware.command.perfil;

import java.util.List;

import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.presenter.perfil.PerfilProjetoPresenter;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

public class CarregarCamposPerfilProjetoCommand implements PerfilProjetoCommand {
    private final PerfilRepositoryService repository;
    private final PerfilProjetoPresenter presenter;

    public CarregarCamposPerfilProjetoCommand(PerfilProjetoPresenter presenter, PerfilRepositoryService repository) {
        this.presenter = presenter;
        this.repository = repository;
    }

    @Override
    public void execute() {
        List<PerfilProjeto> perfis = repository.buscarTodosPerfisPorIdUsuario(
                UsuarioLogadoSingleton.getInstancia().getUsuario().getId());

        for (PerfilProjeto perfil : perfis) {
            presenter.carregarDetalhes(perfil);
        }
    }
}
