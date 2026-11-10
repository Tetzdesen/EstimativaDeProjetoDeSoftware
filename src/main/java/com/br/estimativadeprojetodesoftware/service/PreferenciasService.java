package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.model.Tema;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.IConfiguracaoRepository;
import java.util.Objects;

/**
 * Preferências de interface guardadas na tabela configuracao.
 *
 * O tema é de cada usuário (chave {@code interface.tema.<id>}). A chave
 * {@code interface.tema} guarda o último tema usado neste banco: é o das telas
 * de entrada, antes do login, e o de quem ainda não escolheu um.
 */
public class PreferenciasService {

    public static final String CHAVE_TEMA = "interface.tema";

    private final IConfiguracaoRepository configuracao;

    public PreferenciasService(IConfiguracaoRepository configuracao) {
        this.configuracao = Objects.requireNonNull(configuracao);
    }

    public PreferenciasService() {
        this(SeletorFabricaRepository.obterInstancia().criarConfiguracaoRepository());
    }

    public static String chaveTema(Usuario usuario) {
        return CHAVE_TEMA + "." + usuario.getId();
    }

    /** Último tema usado (telas de entrada); na primeira execução, ou com valor inválido, é o claro. */
    public Tema getTema() {
        return Tema.deTexto(configuracao.buscar(CHAVE_TEMA).orElse(null));
    }

    /** Tema do usuário; quem nunca escolheu fica com o último tema usado. */
    public Tema getTema(Usuario usuario) {
        Objects.requireNonNull(usuario, "usuário");
        return configuracao.buscar(chaveTema(usuario)).map(Tema::deTexto).orElseGet(this::getTema);
    }

    /** Guarda o tema do usuário, que passa a ser também o das telas de entrada. */
    public void setTema(Usuario usuario, Tema tema) {
        Objects.requireNonNull(usuario, "usuário");
        if (tema == null) {
            throw new IllegalArgumentException("Escolha o tema da interface.");
        }
        configuracao.salvar(chaveTema(usuario), tema.name());
        configuracao.salvar(CHAVE_TEMA, tema.name());
    }
}
