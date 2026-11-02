package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.model.Tema;
import com.br.estimativadeprojetodesoftware.repository.IConfiguracaoRepository;
import java.util.Objects;

/** Preferências de interface guardadas na tabela configuracao. */
public class PreferenciasService {

    public static final String CHAVE_TEMA = "interface.tema";

    private final IConfiguracaoRepository configuracao;

    public PreferenciasService(IConfiguracaoRepository configuracao) {
        this.configuracao = Objects.requireNonNull(configuracao);
    }

    public PreferenciasService() {
        this(SeletorFabricaRepository.obterInstancia().criarConfiguracaoRepository());
    }

    /** Tema salvo; na primeira execução (ou valor inválido) é o claro. */
    public Tema getTema() {
        return Tema.deTexto(configuracao.buscar(CHAVE_TEMA).orElse(null));
    }

    public void setTema(Tema tema) {
        if (tema == null) {
            throw new IllegalArgumentException("Escolha o tema da interface.");
        }
        configuracao.salvar(CHAVE_TEMA, tema.name());
    }
}
