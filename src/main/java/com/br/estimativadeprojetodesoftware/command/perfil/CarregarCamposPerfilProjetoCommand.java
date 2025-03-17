package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.command.Command;
import java.util.List;

import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.presenter.perfil.PerfilProjetoPresenter;
import com.br.estimativadeprojetodesoftware.service.PerfilProjetoService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

public class CarregarCamposPerfilProjetoCommand implements Command {
    private final PerfilProjetoService repository;
    private final PerfilProjetoPresenter presenter;

    public CarregarCamposPerfilProjetoCommand(PerfilProjetoPresenter presenter, PerfilProjetoService repository) {
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
