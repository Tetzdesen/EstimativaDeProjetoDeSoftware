package com.br.estimativadeprojetodesoftware.service;

import com.br.authifyjava.AutenticadorSenha;
import com.br.authifyjava.Credenciais;
import com.br.authifyjava.MetodoAutenticacao;
import com.br.authifyjava.ResultadoAutenticacao;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import java.util.List;

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
        //carregarUsuariosNoAutenticador();
    }
    
    public boolean autenticar(String email, String senha){
        Usuario usuario = usuarioRepository.buscarPorEmail(email).get();
        if (usuario == null) {
            return false;
        }
        if (usuario.getSenha().equals(senha)) {
            return true;
        }
        return false;
        //Credenciais credenciais = new Credenciais(email, senha);
        //return metodoAutenticacao.autenticar(credenciais);
    }
    
    private void carregarUsuariosNoAutenticador(){
        List<Usuario> usuarios = usuarioRepository.buscarTodos();
        AutenticadorSenha autenticador = (AutenticadorSenha) metodoAutenticacao;

        for (Usuario usuario : usuarios) {
            autenticador.cadastrarUsuario(usuario.getEmail(), usuario.getSenha());
        }
    }
}
