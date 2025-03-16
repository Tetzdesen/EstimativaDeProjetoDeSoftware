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
    private final UsuarioRepositoryService usuarioService;
    private final ProjetoRepositoryService projetoService;
    private final Usuario usuarioRemetente;
    private final Usuario usuarioDestinatario;
    private final Projeto projeto;

    public CompartilharProjetoCommand(UsuarioRepositoryService usuarioService, ProjetoRepositoryService projetoService, Usuario usuarioRemetente, Usuario usuarioDestinatario, Projeto projeto) {
        this.usuarioService = usuarioService;
        this.projetoService = projetoService;
        this.usuarioRemetente = usuarioRemetente;
        this.usuarioDestinatario = usuarioDestinatario;
        this.projeto = projeto;
    }

    @Override
    public void execute() {
        projeto.adicionarUsuario(usuarioRemetente);
        projeto.setCompartilhado(true);
        projeto.setCompartilhadoPor(usuarioRemetente.getNome());
        usuarioDestinatario.adicionarProjeto(projeto);
        new MostrarMensagemProjetoCommand("Projeto compartilhado com sucesso.").execute();
        
    }
}
