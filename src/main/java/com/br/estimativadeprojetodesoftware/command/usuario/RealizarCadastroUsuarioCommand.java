package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.builder.AndroidBuilder;
import com.br.estimativadeprojetodesoftware.builder.Diretor;
import com.br.estimativadeprojetodesoftware.builder.IosBuilder;
import com.br.estimativadeprojetodesoftware.builder.WebBackEndBuilder;
import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;
import com.br.estimativadeprojetodesoftware.service.ValidadorSenhaService;

/**
 *
 * @author tetzner
 */
public class RealizarCadastroUsuarioCommand implements ProjetoCommand {

    private final UsuarioRepositoryService usuarioService;
    private final ValidadorSenhaService validadorDeSenha;
    private final String nome;
    private final String email;
    private final String senha;
    private final String senhaConfirmada;

    public RealizarCadastroUsuarioCommand(String nome, String email, String senha, String senhaConfirmada) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.senhaConfirmada = senhaConfirmada;
        this.usuarioService = new UsuarioRepositoryService();
        this.validadorDeSenha = new ValidadorSenhaService();
    }

    @Override
    public void execute() {
        Usuario usuario = usuarioService.buscarPorEmail(email).orElse(null);

        if (usuario == null) {

            if (camposInvalidos(email, nome, senha, senhaConfirmada)) {
                throw new IllegalArgumentException("Os campos não podem estar vazios");
            }

            if (senha.equals(senhaConfirmada)) {

                try {
                    if (validadorDeSenha.validarSenha(senha)) {
                        usuario = new Usuario(nome, email, senha);
                        usuarioService.salvar(usuario);
                        
                        PerfilRepositoryService repositoryPerfil = new PerfilRepositoryService();
                        repositoryPerfil.salvar(Diretor.build(new AndroidBuilder("Android", usuario)));
                        repositoryPerfil.salvar(Diretor.build(new IosBuilder("iOS", usuario)));
                        repositoryPerfil.salvar(Diretor.build(new WebBackEndBuilder("Web Back-End", usuario)));
                        
                        exibirMensagem("Cadastro realizado com sucesso!");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException("Erro na validação de senha: " + ex.getMessage());
                }

            } else {
                throw new IllegalArgumentException("Senhas não conferem");
            }
        } else {
            throw new IllegalArgumentException("Usuário já cadastrado no sistema, por favor cadastrar outro usuário");
        }
    }

    private boolean camposInvalidos(String email, String nome, String senha, String senhaConfirmada) {
        return email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty() || nome == null
                || nome.trim().isEmpty() || senhaConfirmada == null || senhaConfirmada.isEmpty();
    }

    private void exibirMensagem(String mensagem) {
        new MostrarMensagemProjetoCommand(mensagem).execute();
    }

}
