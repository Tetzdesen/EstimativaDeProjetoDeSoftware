package com.br.estimativadeprojetodesoftware.presenter.tabela;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.CustoAdicional;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.suporte.Cenarios;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Só uma mudança real feita pelo usuário avisa o presenter. */
class NotificacaoAlteracaoTest {

    private Projeto projeto;
    private AtomicInteger avisos;
    private List<String> erros;

    @BeforeEach
    void preparar() {
        projeto = Cenarios.projetoVazio(Plataforma.WEB_BACKEND);
        avisos = new AtomicInteger();
        erros = new ArrayList<>();
    }

    private ItensProjetoTableModel itens() {
        ItensProjetoTableModel m = new ItensProjetoTableModel(projeto, erros::add);
        m.setAoAlterar(avisos::incrementAndGet);
        m.setEditavel(true);
        return m;
    }

    private TaxasTableModel taxas() {
        TaxasTableModel m = new TaxasTableModel(projeto::getTaxas, erros::add);
        m.setAoAlterar(avisos::incrementAndGet);
        m.setEditavel(true);
        return m;
    }

    private CustosTableModel custos() {
        CustosTableModel m = new CustosTableModel(projeto, erros::add);
        m.setAoAlterar(avisos::incrementAndGet);
        m.setEditavel(true);
        return m;
    }

    private static int linhaComValorNumerico(ItensProjetoTableModel m) {
        for (int i = 0; i < m.getRowCount(); i++) {
            if (!FormatoValor.NA.equals(m.getValueAt(i, 4))) {
                return i;
            }
        }
        throw new IllegalStateException("Perfil sem valor na coluna Web");
    }

    @Test
    void itensAvisamSoQuandoOValorMuda() {
        ItensProjetoTableModel m = itens();
        int linha = linhaComValorNumerico(m);
        Object atual = m.getValueAt(linha, 4);

        m.setValueAt(atual, linha, 4);
        assertEquals(0, avisos.get(), "confirmar o mesmo valor");

        m.setValueAt("123", linha, 4);
        assertEquals(1, avisos.get());

        m.setValueAt(true, linha, 0);
        assertEquals(2, avisos.get());

        m.setValueAt(true, linha, 0);
        assertEquals(2, avisos.get(), "marcar o que já está marcado");
    }

    @Test
    void itensNaoAvisamComValorInvalido() {
        ItensProjetoTableModel m = itens();
        m.setValueAt("abc", linhaComValorNumerico(m), 4);
        assertEquals(0, avisos.get());
        assertEquals(1, erros.size());
    }

    @Test
    void taxasAvisamSoQuandoOValorMuda() {
        TaxasTableModel m = taxas();
        m.setValueAt(m.getValueAt(0, 1), 0, 1);
        assertEquals(0, avisos.get(), "confirmar o mesmo valor");

        m.setValueAt("987,65", 0, 1);
        assertEquals(1, avisos.get());

        m.setValueAt("abc", 0, 1);
        assertEquals(1, avisos.get(), "valor inválido");
        assertEquals(1, erros.size());
    }

    @Test
    void custosAvisamSoQuandoOValorMuda() {
        CustosTableModel m = custos();
        projeto.adicionarCustoAdicional(new CustoAdicional("Extra", java.math.BigDecimal.TEN));
        int linha = m.getRowCount() - 1;

        m.setValueAt(m.getValueAt(linha, 0), linha, 0);
        m.setValueAt(m.getValueAt(linha, 1), linha, 1);
        assertEquals(0, avisos.get(), "confirmar os mesmos valores");

        m.setValueAt("Outro nome", linha, 0);
        assertEquals(1, avisos.get());
        m.setValueAt("20,00", linha, 1);
        assertEquals(2, avisos.get());

        m.setValueAt("abc", linha, 1);
        assertEquals(2, avisos.get(), "valor inválido");
    }

    @Test
    void mudarOModoDeEdicaoNaoAvisa() {
        itens().setEditavel(false);
        taxas().setEditavel(false);
        custos().setEditavel(false);
        assertEquals(0, avisos.get());
    }

    @Test
    void contaSelecionados() {
        ItensProjetoTableModel m = itens();
        assertEquals(0, m.contarSelecionados());
        int linha = linhaComValorNumerico(m);
        m.setValueAt(true, linha, 0);
        assertEquals(1, m.contarSelecionados());
        assertTrue(m.isSelecionado(linha));
    }
}
