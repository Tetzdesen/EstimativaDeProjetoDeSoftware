package com.br.estimativadeprojetodesoftware.service;

import com.br.authifyjava.AutenticadorSenha;
import com.br.authifyjava.Credenciais;
import com.br.authifyjava.MetodoAutenticacao;
import com.br.authifyjava.ResultadoAutenticacao;
import java.util.Map;

/**
 *
 * @author tetzner
 */
public class AutenticacaoService {

    private final MetodoAutenticacao metodoAutenticacao;
    private final UsuarioService usuarioRepository;

    public AutenticacaoService() {
        this.usuarioRepository = new UsuarioService();
        this.metodoAutenticacao = new AutenticadorSenha();
        carregarUsuariosNoAutenticador();
    }

    public ResultadoAutenticacao autenticar(String email, String senha) {
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
