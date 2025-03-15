package com.br.estimativadeprojetodesoftware.service;

import com.br.authifyjava.AutenticadorSenha;
import com.br.authifyjava.Credenciais;
import com.br.authifyjava.MetodoAutenticacao;
import com.br.authifyjava.ResultadoAutenticacao;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author tetzner
 */
public class AutenticacaoService {

    private MetodoAutenticacao metodoAutenticacao;
    private UsuarioRepositoryService usuarioRepository;

    public AutenticacaoService() {
        this.usuarioRepository = new UsuarioRepositoryService();
        this.metodoAutenticacao = new AutenticadorSenha();
        carregarUsuariosNoAutenticador();
    }

    public ResultadoAutenticacao autenticar(String email, String senha) {
        /*
        Optional<Usuario> usuario = usuarioRepository.buscarPorEmail(email);
        if (!usuario.isPresent()) {
            throw new RuntimeException("Usuário não encontrado");
        } else {
            if (usuario.get().getSenha().equals(senha)) {
                return true;
            }
        }*/

     //   throw new RuntimeException("E-mail ou senha não conferem");
        Credenciais credenciais = new Credenciais(email, senha);
        return metodoAutenticacao.autenticar(credenciais);
    }

    private void carregarUsuariosNoAutenticador() {
        Map<String, String> usuarios = usuarioRepository.buscarEmailESenhaDeUsuarios();
        AutenticadorSenha autenticador = (AutenticadorSenha) metodoAutenticacao;

        for (Map.Entry<String, String> usuario : usuarios.entrySet()) {
            autenticador.cadastrarUsuario(usuario.getKey(), usuario.getValue());
        }
    }
}
