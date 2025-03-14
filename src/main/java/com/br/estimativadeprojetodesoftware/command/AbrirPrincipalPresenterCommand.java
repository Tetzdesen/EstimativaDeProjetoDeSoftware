package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;

/**
 *
 * @author tetzner
 */
public class AbrirPrincipalPresenterCommand implements ProjetoCommand {

    private final ProjetoRepositoryService projetoService;
    private final UsuarioRepositoryMock usuarioRepository;

    public AbrirPrincipalPresenterCommand() {
        this.projetoService = new ProjetoRepositoryService();
        this.usuarioRepository = new UsuarioRepositoryMock();
    }
        
    @Override
    public void execute() {
        PrincipalProjetoPresenter presenter = new PrincipalProjetoPresenter(projetoService, usuarioRepository);
        WindowManager.getInstance().initialize(presenter);
    }

}
