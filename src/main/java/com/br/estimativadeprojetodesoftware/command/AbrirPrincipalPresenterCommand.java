package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;

/**
 *
 * @author tetzner
 */
public class AbrirPrincipalPresenterCommand implements ProjetoCommand {

    private final ProjetoRepositoryMock projetoRepository;
    private final UsuarioRepositoryMock usuarioRepository;
    private final PerfilRepositoryMock perfilRepository;

    public AbrirPrincipalPresenterCommand() {
        this.projetoRepository = new ProjetoRepositoryMock();
        this.usuarioRepository = new UsuarioRepositoryMock();
        this.perfilRepository = new PerfilRepositoryMock();
    }
        
    @Override
    public void execute() {
        PrincipalPresenter presenter = new PrincipalPresenter(projetoRepository, usuarioRepository, perfilRepository);
        WindowManager.getInstance().initialize(presenter);
    }

}
