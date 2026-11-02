package com.br.estimativadeprojetodesoftware.seeder;

import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.Papel;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.TaxaDiaria;
import com.br.estimativadeprojetodesoftware.model.TipoSelecao;
import com.br.estimativadeprojetodesoftware.model.TipoValor;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Carrega o perfil padrão do EstimateMyApp a partir de seed/perfil-padrao.csv.
 * "NA" vira "não aplicável" (diferente de 0). Não faz nada se o perfil padrão já existir.
 */
public class PerfilPadraoSeeder {

    public static final String RECURSO = "/seed/perfil-padrao.csv";
    public static final String NOME_PERFIL = "Perfil padrão (EstimateMyApp)";
    private static final Plataforma[] COLUNAS = {Plataforma.WEB_BACKEND, Plataforma.IOS, Plataforma.ANDROID};

    private final IPerfilRepository repository;

    public PerfilPadraoSeeder(IPerfilRepository repository) {
        this.repository = repository;
    }

    public void executar() {
        if (!repository.existePerfilPadrao()) {
            repository.salvar(lerPerfil());
        }
    }

    public static Perfil lerPerfil() {
        Perfil perfil = new Perfil(UUID.randomUUID(), NOME_PERFIL, true, null, new TaxaDiaria(), LocalDateTime.now());
        try (InputStream in = PerfilPadraoSeeder.class.getResourceAsStream(RECURSO)) {
            if (in == null) {
                throw new IllegalStateException("Arquivo do perfil padrão não encontrado: " + RECURSO);
            }
            BufferedReader leitor = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            leitor.readLine(); // cabeçalho
            String linha;
            int ordem = 0;
            while ((linha = leitor.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] c = linha.split(";", -1);
                if (c.length != 7) {
                    throw new IllegalStateException("Linha inválida no perfil padrão: " + linha);
                }
                if ("TAXA".equals(c[1])) {
                    Papel papel = Papel.deDescricao(c[3]);
                    for (int i = 0; i < COLUNAS.length; i++) {
                        perfil.getTaxas().set(COLUNAS[i], papel, new BigDecimal(c[4 + i].trim()));
                    }
                    continue;
                }
                Funcionalidade f = new Funcionalidade(c[0], c[3], TipoSelecao.valueOf(c[1].trim()),
                        TipoValor.valueOf(c[2].trim()), ++ordem);
                for (int i = 0; i < COLUNAS.length; i++) {
                    String valor = c[4 + i].trim();
                    f.setValor(COLUNAS[i], "NA".equalsIgnoreCase(valor) ? null : new BigDecimal(valor));
                }
                perfil.adicionarFuncionalidade(f);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Erro ao ler o perfil padrão", e);
        }
        return perfil;
    }
}
