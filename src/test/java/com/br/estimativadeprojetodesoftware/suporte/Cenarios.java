package com.br.estimativadeprojetodesoftware.suporte;

import com.br.estimativadeprojetodesoftware.model.CustoAdicional;
import com.br.estimativadeprojetodesoftware.model.Papel;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.seeder.PerfilPadraoSeeder;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/** Projetos de teste montados a partir do perfil padrão do CSV. */
public final class Cenarios {

    private Cenarios() {
    }

    public static Perfil perfilPadrao() {
        return PerfilPadraoSeeder.lerPerfil();
    }

    public static Projeto projetoVazio(Plataforma... plataformas) {
        return projetoVazio(UUID.randomUUID(), plataformas);
    }

    public static Projeto projetoVazio(UUID dono, Plataforma... plataformas) {
        Projeto p = Projeto.aPartirDePerfis("Teste", dono, List.of(perfilPadrao()));
        for (Plataforma pl : Plataforma.values()) {
            p.setPlataformaMarcada(pl, false);
        }
        for (Plataforma pl : plataformas) {
            p.setPlataformaMarcada(pl, true);
        }
        return p;
    }

    public static void marcar(Projeto p, String categoria, String nome) {
        p.selecionar(p.buscarItem(categoria, nome).orElseThrow(
                () -> new IllegalArgumentException("Item não existe: " + categoria + " / " + nome)).getFuncionalidade().getId(), true);
    }

    /** Protótipo da planilha (Tabela 1): Web/Back-end + Android. */
    public static Projeto prototipoPlanilha() {
        return prototipoPlanilha(UUID.randomUUID());
    }

    public static Projeto prototipoPlanilha(UUID dono) {
        Projeto p = projetoVazio(dono, Plataforma.WEB_BACKEND, Plataforma.ANDROID);
        marcar(p, "Tamanho do App", "Grande");
        marcar(p, "Nível de UI", "Profissional");
        marcar(p, "Gerente de Projeto", "Gerente de Projeto");
        String u = "Usuários e Contas";
        marcar(p, u, "Cadastro por E-mail e Senha");
        marcar(p, u, "Cadastro pelo Facebook");
        marcar(p, u, "Cadastro pelo Twitter");
        marcar(p, u, "E-mails de Convite de Usuário");
        marcar(p, u, "Contas Multi-tenant");
        String c = "Conteúdo Gerado pelo Usuário";
        marcar(p, c, "Painel (Dashboard)");
        marcar(p, c, "E-mails Transacionais");
        marcar(p, c, "Tags");
        marcar(p, c, "Pesquisa de Texto Livre");
        marcar(p, "Social e Engajamento", "Fóruns ou Comentários");
        marcar(p, "Administração, Feedback e Análises", "Suporte Multilíngue");
        marcar(p, "Segurança", "Segurança baseada em Certificado SSL");
        marcar(p, "Segurança", "Proteção contra DoS");
        marcar(p, "Segurança", "Autenticação em Duas Etapas");
        marcar(p, "Recursos Móveis Específicos (Android)", "Códigos de Barras ou QR Codes");
        for (Plataforma pl : Plataforma.values()) {
            p.getTaxas().set(pl, Papel.DESENVOLVIMENTO, new BigDecimal("450.00"));
            p.getTaxas().set(pl, Papel.DESIGN, new BigDecimal("550.00"));
            p.getTaxas().set(pl, Papel.GERENCIA, new BigDecimal("300.00"));
        }
        p.substituirCustosAdicionais(List.of(
                new CustoAdicional("Hardware e instalações físicas", new BigDecimal("1200.00")),
                new CustoAdicional("Softwares", new BigDecimal("2000.00")),
                new CustoAdicional("Fundo de reserva", new BigDecimal("2000.00"))));
        p.setPercentualImpostos(new BigDecimal("0.42"));
        p.setPercentualLucro(new BigDecimal("0.30"));
        return p;
    }
}
