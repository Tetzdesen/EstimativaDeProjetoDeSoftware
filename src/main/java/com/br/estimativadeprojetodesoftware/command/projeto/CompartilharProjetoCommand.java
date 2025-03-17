package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.service.UsuarioService;
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author flamo
 */
public class CompartilharProjetoCommand implements Command {
    private final ProjetoService repository;
    private final String email;
    private final String nomeProjeto;

    public CompartilharProjetoCommand(ProjetoService repository, String email, String nomeProjeto) {
        this.repository = repository;
        this.email = email;
        this.nomeProjeto = nomeProjeto;
    }

    @Override
    public void execute() {
        Usuario usuarioCompartilhado = new UsuarioService().buscarPorEmail(email).get();

        Projeto projeto = new ProjetoService().buscarProjetoPorNome(nomeProjeto).get();
        projeto.setCompartilhado(true);
        projeto.adicionarUsuario(usuarioCompartilhado);

        usuarioCompartilhado.adicionarProjeto(projeto);

        repository.salvar(projeto, usuarioCompartilhado);

        new MostrarMensagemProjetoCommand("Projeto compartilhado com sucesso.").execute();
    }
}
