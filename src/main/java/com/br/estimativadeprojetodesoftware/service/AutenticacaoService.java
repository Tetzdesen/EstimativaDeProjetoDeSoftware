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
    private final Map<String, String> hashesPorEmail;

    public AutenticacaoService() {
        this(new UsuarioService());
    }

    public AutenticacaoService(UsuarioService usuarios) {
        this.usuarioRepository = usuarios;
        this.metodoAutenticacao = new AutenticadorSenha();
        this.hashesPorEmail = usuarioRepository.buscarEmailESenhaDeUsuarios();
        carregarUsuariosNoAutenticador();
    }

    public ResultadoAutenticacao autenticar(String email, String senha) {
        Credenciais credenciais = new Credenciais(email, calcularSenhaComparavel(email, senha));
        return metodoAutenticacao.autenticar(credenciais);
    }

    /**
     * O autenticador compara os valores cadastrados, então a senha digitada
     * precisa ser convertida no mesmo hash guardado para aquele usuário.
     */
    private String calcularSenhaComparavel(String email, String senha) {
        String hashArmazenado = hashesPorEmail.get(email);

        if (senha == null || !SenhaHashService.estaHasheada(hashArmazenado)) {
            return senha;
        }
        return SenhaHashService.recalcularHash(senha, hashArmazenado);
    }

    private void carregarUsuariosNoAutenticador() {
        AutenticadorSenha autenticador = (AutenticadorSenha) metodoAutenticacao;

        for (Map.Entry<String, String> usuario : hashesPorEmail.entrySet()) {
            // Usuários com senha fora do formato de hash não são cadastrados,
            // de modo que nenhuma senha em texto puro consiga autenticar.
            if (SenhaHashService.estaHasheada(usuario.getValue())) {
                autenticador.cadastrarUsuario(usuario.getKey(), usuario.getValue());
            }
        }
    }
}
