package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.adapter.senha.IValidadorSenha;
import com.br.estimativadeprojetodesoftware.adapter.senha.ValidadorSenhaAdapter;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio.Entidade;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio.Tipo;
import com.br.estimativadeprojetodesoftware.observer.Subject;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/** Cadastro e manutenção de usuários (caso de uso Criar Novo Usuário). */
public class UsuarioService extends ServicoObservavel {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final IUsuarioRepository usuarioRepository;
    private final IValidadorSenha validadorSenha;

    public UsuarioService(IUsuarioRepository usuarioRepository, IValidadorSenha validadorSenha, Subject publicador) {
        super(publicador);
        this.usuarioRepository = usuarioRepository;
        this.validadorSenha = validadorSenha;
    }

    public UsuarioService() {
        this(SeletorFabricaRepository.obterInstancia().criarUsuarioRepository(), new ValidadorSenhaAdapter(), null);
    }

    /**
     * Cria a conta com e-mail único e senha aceita pelo validador. A senha é
     * gravada como hash PBKDF2.
     */
    public Usuario cadastrar(String nome, String email, String senha, String confirmacao) {
        if (vazio(nome) || vazio(email) || vazio(senha) || vazio(confirmacao)) {
            throw new IllegalArgumentException("Preencha nome, e-mail, senha e confirmação.");
        }
        String emailNormalizado = email.trim();
        if (!EMAIL.matcher(emailNormalizado).matches()) {
            throw new IllegalArgumentException("Informe um e-mail válido.");
        }
        if (usuarioRepository.buscarPorEmail(emailNormalizado).isPresent()) {
            throw new IllegalArgumentException("O e-mail " + emailNormalizado + " já está cadastrado. Use outro e-mail ou faça login.");
        }
        if (!senha.equals(confirmacao)) {
            throw new IllegalArgumentException("As senhas não conferem.");
        }
        validarSenha(senha);
        Usuario usuario = new Usuario(nome.trim(), emailNormalizado, SenhaHashService.gerarHash(senha));
        usuarioRepository.salvar(usuario);
        publicar(Tipo.CRIACAO, Entidade.USUARIO, usuario.getId());
        return usuario;
    }

    /** Lança exceção com as mensagens do validador quando a senha é recusada. */
    public void validarSenha(String senha) {
        List<String> erros = validadorSenha.validar(senha);
        if (!erros.isEmpty()) {
            throw new IllegalArgumentException("A senha não atende às regras:\n" + String.join("\n", erros));
        }
    }

    public void atualizar(Usuario usuario) {
        usuarioRepository.buscarPorEmail(usuario.getEmail())
                .filter(outro -> !outro.getId().equals(usuario.getId()))
                .ifPresent(outro -> {
                    throw new IllegalArgumentException("O e-mail " + usuario.getEmail() + " já pertence a outro usuário.");
                });
        usuarioRepository.atualizar(usuario);
        publicar(Tipo.ALTERACAO, Entidade.USUARIO, usuario.getId());
    }

    public boolean removerPorId(UUID id) {
        boolean removido = usuarioRepository.removerPorId(id);
        publicar(Tipo.EXCLUSAO, Entidade.USUARIO, id);
        return removido;
    }

    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.buscarPorId(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
    }

    public List<Usuario> buscarTodos() {
        return usuarioRepository.buscarTodos();
    }

    public Map<String, String> buscarEmailESenhaDeUsuarios() {
        return usuarioRepository.buscarEmailESenhaDeUsuarios();
    }

    private static boolean vazio(String s) {
        return s == null || s.isBlank();
    }
}
