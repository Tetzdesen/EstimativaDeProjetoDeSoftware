package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;

/**
 *
 * @author flamo
 */
public class CompartilharProjetoCommand implements ProjetoCommand {
    private final UsuarioRepositoryMock usuarioRepository;
    private final ProjetoRepositoryMock projetoRepository;
    private Usuario usuarioRemetente;
    private Usuario usuarioDestinatario;
    private Projeto projeto;

    public CompartilharProjetoCommand(UsuarioRepositoryMock usuarioRepository, ProjetoRepositoryMock projetoRepository, Usuario usuarioRemetente, Usuario usuarioDestinatario, Projeto projeto) {
        this.usuarioRepository = usuarioRepository;
        this.projetoRepository = projetoRepository;
        this.usuarioRemetente = usuarioRemetente;
        this.usuarioDestinatario = usuarioDestinatario;
        this.projeto = projeto;
    }

    @Override
    public void execute() {
        projeto.setCompartilhadoPor(usuarioRemetente.getNome());
        projeto.adicionarUsuario(usuarioRemetente);
        projeto.setCompartilhado(true);
        projeto.setCompartilhadoPor(usuarioRemetente.getNome());
        usuarioDestinatario.adicionarProjeto(projeto);
        new MostrarMensagemProjetoCommand("Projeto compartilhado com sucesso.").execute();
    }
}
