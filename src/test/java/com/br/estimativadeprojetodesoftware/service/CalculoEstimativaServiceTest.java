package com.br.estimativadeprojetodesoftware.service;

import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.arredondarDias;
import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.arredondarReais;
import static com.br.estimativadeprojetodesoftware.suporte.Cenarios.marcar;
import static com.br.estimativadeprojetodesoftware.suporte.Cenarios.projetoVazio;
import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.CustoAdicional;
import com.br.estimativadeprojetodesoftware.model.ItemProjeto;
import com.br.estimativadeprojetodesoftware.model.Papel;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.suporte.Cenarios;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculoEstimativaServiceTest {

    private final CalculoEstimativaService calculo = new CalculoEstimativaService();

    private static void assertValor(String esperado, BigDecimal valor) {
        assertEquals(new BigDecimal(esperado), arredondarReais(valor));
    }

    @Test
    @DisplayName("8.5 Caso de aceitação da planilha (Tabela 1)")
    void casoDeAceitacaoDaPlanilha() {
        ResultadoEstimativa r = calculo.calcular(Cenarios.prototipoPlanilha());

        var web = r.getPorPlataforma().get(Plataforma.WEB_BACKEND);
        var android = r.getPorPlataforma().get(Plataforma.ANDROID);
        assertNull(r.getPorPlataforma().get(Plataforma.IOS));
        assertEquals(new BigDecimal("129.33"), arredondarDias(web.dias()));
        assertValor("59033.33", web.custo());
        assertEquals(new BigDecimal("111.33"), arredondarDias(android.dias()));
        assertValor("50933.33", android.custo());
        assertEquals(new BigDecimal("240.67"), arredondarDias(r.getDiasTotal()));
        assertValor("109966.67", r.getCustoDesenvolvimento());

        assertValor("5200.00", r.getTotalCustosAdicionais());
        assertValor("115166.67", r.getCustoTotal());
        assertValor("48370.00", r.getImposto());
        assertValor("163536.67", r.getValorComImposto());
        assertValor("49061.00", r.getLucro());
        assertValor("212597.67", r.getPrecoFinal());

        assertEquals(241, r.getDiasTotalExibicao());
        assertEquals(new BigDecimal("8.0"), r.getMesesExibicao());
        assertValor("26501.09", r.getMediaMensal());
        assertEquals("R$ 212.597,67", ResultadoEstimativa.formatarReais(r.getPrecoFinal()));
    }

    @Test
    @DisplayName("Precisão: total igual à soma exata das plataformas, sem erro de arredondamento")
    void somaConsistente() {
        ResultadoEstimativa r = calculo.calcular(Cenarios.prototipoPlanilha());
        BigDecimal soma = r.getPorPlataforma().values().stream().map(d -> d.dias()).reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(0, soma.compareTo(r.getDiasTotal()));
        BigDecimal somaCategorias = r.getPorCategoria().values().stream().map(c -> c.custo()).reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(arredondarReais(r.getCustoDesenvolvimento()), arredondarReais(somaCategorias));
    }

    private BigDecimal diasUI(Projeto p) {
        return arredondarDias(calculo.calcular(p).getPorPlataforma().values().stream()
                .map(d -> d.diasUI()).reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    @Test
    @DisplayName("8.6 UI com uma plataforma: Pequeno 10 dias x 70% / 3 = 2,33 dias")
    void uiUmaPlataforma() {
        Projeto p = projetoVazio(Plataforma.IOS);
        marcar(p, "Tamanho do App", "Pequeno");
        marcar(p, "Nível de UI", "Profissional");
        assertEquals(new BigDecimal("2.33"), diasUI(p));
    }

    @Test
    @DisplayName("8.6 UI com duas plataformas: Grande, 46,67 dias (23,33 por plataforma)")
    void uiDuasPlataformas() {
        Projeto p = projetoVazio(Plataforma.WEB_BACKEND, Plataforma.ANDROID);
        marcar(p, "Tamanho do App", "Grande");
        marcar(p, "Nível de UI", "Profissional");
        assertEquals(new BigDecimal("46.67"), diasUI(p));
        calculo.calcular(p).getPorPlataforma().values()
                .forEach(d -> assertEquals(new BigDecimal("23.33"), arredondarDias(d.diasUI())));
    }

    @Test
    @DisplayName("8.6 UI com três plataformas: Pequeno, 21 dias (7 por plataforma)")
    void uiTresPlataformas() {
        Projeto p = projetoVazio(Plataforma.values());
        marcar(p, "Tamanho do App", "Pequeno");
        marcar(p, "Nível de UI", "Profissional");
        assertEquals(new BigDecimal("21.00"), diasUI(p));
    }

    @Test
    @DisplayName("8.6 Funcionalidade conta uma vez em cada plataforma marcada")
    void contagemEmDuasPlataformas() {
        Projeto p = projetoVazio(Plataforma.WEB_BACKEND, Plataforma.ANDROID);
        marcar(p, "Usuários e Contas", "Cadastro por E-mail e Senha");
        assertEquals(new BigDecimal("2.00"), arredondarDias(calculo.calcular(p).getDiasTotal()));
    }

    @Test
    @DisplayName("Multiplataforma sem back-end soma apenas iOS e Android")
    void semBackend() {
        Projeto p = projetoVazio(Plataforma.IOS, Plataforma.ANDROID);
        marcar(p, "Usuários e Contas", "E-mails de Convite de Usuário"); // NA em iOS e Android
        marcar(p, "Usuários e Contas", "Cadastro por E-mail e Senha");
        ResultadoEstimativa r = calculo.calcular(p);
        assertEquals(2, r.getPorPlataforma().size());
        assertEquals(new BigDecimal("2.00"), arredondarDias(r.getDiasTotal()));
    }

    @Test
    @DisplayName("8.6 Taxa de design R$ 550 só em iOS e Android mantém a taxa da Web")
    void taxaDiferentePorPlataforma() {
        Projeto p = projetoVazio(Plataforma.values());
        marcar(p, "Tamanho do App", "Pequeno");
        marcar(p, "Nível de UI", "Profissional");
        p.getTaxas().set(Plataforma.IOS, Papel.DESIGN, new BigDecimal("550"));
        p.getTaxas().set(Plataforma.ANDROID, Papel.DESIGN, new BigDecimal("550"));
        ResultadoEstimativa r = calculo.calcular(p);
        // por plataforma: 10 dias dev x 450 + 7 dias UI x taxa de design
        assertValor("7650.00", r.getPorPlataforma().get(Plataforma.WEB_BACKEND).custo());
        assertValor("8350.00", r.getPorPlataforma().get(Plataforma.IOS).custo());
        assertValor("8350.00", r.getPorPlataforma().get(Plataforma.ANDROID).custo());
    }

    @Test
    @DisplayName("8.2 Sem plataforma marcada o cálculo é recusado com o motivo")
    void nenhumaPlataforma() {
        Projeto p = projetoVazio();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> calculo.calcular(p));
        assertTrue(e.getMessage().contains("ao menos uma plataforma"));
    }

    @Test
    @DisplayName("8.2 Tamanho do App é escolha única: Grande desmarca Pequeno")
    void trocaDeTamanho() {
        Projeto p = projetoVazio(Plataforma.WEB_BACKEND);
        marcar(p, "Tamanho do App", "Pequeno");
        marcar(p, "Tamanho do App", "Grande");
        assertFalse(p.buscarItem("Tamanho do App", "Pequeno").orElseThrow().isSelecionado());
        assertTrue(p.buscarItem("Tamanho do App", "Grande").orElseThrow().isSelecionado());
        marcar(p, "Nível de UI", "MVP");
        marcar(p, "Nível de UI", "Básico");
        assertEquals(1, p.getItens().stream().filter(i -> i.isSelecionado()
                && i.getFuncionalidade().getCategoria().equals("Nível de UI")).count());
    }

    @Test
    @DisplayName("8.2 Item não aplicável não soma dias na plataforma; item NA em todas não pode ser marcado")
    void naoAplicavel() {
        Projeto p = projetoVazio(Plataforma.IOS);
        marcar(p, "Faturamento e e-Commerce", "Planos de Assinatura"); // só Web
        assertEquals(0, calculo.calcular(p).getDiasTotal().signum());

        ItemProjeto item = p.buscarItem("Faturamento e e-Commerce", "Planos de Assinatura").orElseThrow();
        for (Plataforma pl : Plataforma.values()) {
            item.getFuncionalidade().marcarNaoAplicavel(pl);
        }
        assertThrows(IllegalArgumentException.class, () -> p.selecionar(item.getFuncionalidade().getId(), true));
    }

    @Test
    @DisplayName("Relatório de Erros na Web é aceito e soma 0 dias")
    void zeroDias() {
        Projeto p = projetoVazio(Plataforma.WEB_BACKEND);
        marcar(p, "Administração, Feedback e Análises", "Relatório de Erros");
        assertEquals(0, calculo.calcular(p).getDiasTotal().signum());
    }

    @Test
    @DisplayName("Desenvolvimento específico soma os dias digitados")
    void desenvolvimentoEspecifico() {
        Projeto p = projetoVazio(Plataforma.WEB_BACKEND);
        p.buscarItem("Desenvolvimento Específico do App", "Desenvolvimento Específico do Aplicativo").orElseThrow()
                .getFuncionalidade().setValor(Plataforma.WEB_BACKEND, new BigDecimal("12.5"));
        assertEquals(new BigDecimal("12.50"), arredondarDias(calculo.calcular(p).getDiasTotal()));
    }

    @Test
    @DisplayName("8.3 Custos adicionais: itens iniciais da BR11, item novo e item zerado")
    void custosAdicionais() {
        Projeto p = projetoVazio(Plataforma.WEB_BACKEND);
        List<String> descricoes = p.getCustosAdicionais().stream().map(CustoAdicional::getDescricao).toList();
        assertEquals(List.of("Hardware e instalações físicas", "Softwares", "Custo com riscos", "Garantia", "Transporte",
                "Outros custos"), descricoes);
        assertValor("12000.00", calculo.calcular(p).getTotalCustosAdicionais());

        p.adicionarCustoAdicional(new CustoAdicional("Fundo de reserva", new BigDecimal("2000.00")));
        assertValor("14000.00", calculo.calcular(p).getTotalCustosAdicionais());

        p.getCustosAdicionais().stream().filter(c -> c.getDescricao().equals("Custo com riscos")).findFirst()
                .orElseThrow().setValor(BigDecimal.ZERO);
        assertValor("12500.00", calculo.calcular(p).getTotalCustosAdicionais());
    }

    @Test
    @DisplayName("8.3 Impostos e lucro configuráveis por projeto")
    void impostosELucro() {
        Projeto p = projetoVazio(Plataforma.WEB_BACKEND);
        p.substituirCustosAdicionais(List.of(new CustoAdicional("Único", new BigDecimal("1000"))));
        p.setPercentualImpostos(new BigDecimal("0.10"));
        p.setPercentualLucro(new BigDecimal("0.20"));
        ResultadoEstimativa r = calculo.calcular(p);
        assertValor("100.00", r.getImposto());
        assertValor("1320.00", r.getPrecoFinal());
    }
}
