package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;

/**
 *
 * @author flamo
 */
public class CompartilharProjetoCommand implements ProjetoCommand {
    private final ProjetoRepositoryService repository;
    private final String email;
    private final String nomeProjeto;

    public CompartilharProjetoCommand(ProjetoRepositoryService repository, String email, String nomeProjeto) {
        this.repository = repository;
        this.email = email;
        this.nomeProjeto = nomeProjeto;
    }

    @Override
    public void execute() {
        Usuario usuarioCompartilhado = new UsuarioRepositoryService().buscarPorEmail(email).get();

        Projeto projeto = new ProjetoRepositoryService().buscarProjetoPorNome(nomeProjeto).get();
        projeto.setCompartilhado(true);
        projeto.adicionarUsuario(usuarioCompartilhado);

        usuarioCompartilhado.adicionarProjeto(projeto);

        repository.salvar(projeto, usuarioCompartilhado);

        new MostrarMensagemProjetoCommand("Projeto compartilhado com sucesso.").execute();
        
    }
}
