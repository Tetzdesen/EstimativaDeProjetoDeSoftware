package com.br.estimativadeprojetodesoftware.presenter.tabela;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FiltroFuncionalidadesTest {

    private TableRowSorter<TableModel> ordenador;

    @BeforeEach
    void preparar() {
        DefaultTableModel modelo = new DefaultTableModel(new Object[][]{
            {true, "Usuários e Contas", "Cadastro por E-mail"},
            {false, "Usuários e Contas", "Cadastro pelo Twitter"},
            {true, "Segurança", "Proteção contra DoS"},
            {false, "Segurança", "Cadastro seguro"}}, new Object[]{"Selecionar", "Categoria", "Funcionalidade"}) {
            @Override
            public Class<?> getColumnClass(int coluna) {
                return coluna == 0 ? Boolean.class : String.class;
            }
        };
        ordenador = new TableRowSorter<>(modelo);
    }

    private int linhas(String texto, boolean somenteSelecionadas) {
        ordenador.setRowFilter(new FiltroFuncionalidades(texto, somenteSelecionadas));
        return ordenador.getViewRowCount();
    }

    @Test
    void semFiltroMostraTudo() {
        assertEquals(4, linhas("", false));
        assertEquals(4, linhas("   ", false));
        assertEquals(4, linhas(null, false));
    }

    @Test
    void buscaNoNomeENaCategoriaSemDiferenciarCaixa() {
        assertEquals(3, linhas("cadastro", false));
        assertEquals(3, linhas("CADASTRO", false));
        assertEquals(2, linhas("segurança", false));
        assertEquals(0, linhas("inexistente", false));
    }

    @Test
    void somenteSelecionadasCombinaComABusca() {
        assertEquals(2, linhas("", true));
        assertEquals(1, linhas("cadastro", true));
    }

    private int linhas(String texto, boolean somenteSelecionadas, String categoria) {
        ordenador.setRowFilter(new FiltroFuncionalidades(texto, somenteSelecionadas, categoria));
        return ordenador.getViewRowCount();
    }

    @Test
    void categoriaSozinhaSemDiferenciarCaixa() {
        assertEquals(2, linhas("", false, "Segurança"));
        assertEquals(2, linhas("", false, "SEGURANÇA"));
        assertEquals(4, linhas("", false, null));
        assertEquals(0, linhas("", false, "Inexistente"));
    }

    @Test
    void categoriaCombinaComABusca() {
        assertEquals(2, linhas("cadastro", false, "Usuários e Contas"));
        assertEquals(1, linhas("cadastro", false, "Segurança"));
        assertEquals(0, linhas("twitter", false, "Segurança"));
    }

    @Test
    void categoriaCombinaComSomenteSelecionadas() {
        assertEquals(1, linhas("", true, "Segurança"));
        assertEquals(1, linhas("", true, "Usuários e Contas"));
    }

    @Test
    void categoriasExcluidasNuncaAparecem() {
        java.util.List<String> fora = java.util.List.of("SEGURANÇA");
        ordenador.setRowFilter(new FiltroFuncionalidades("", false, null, fora));
        assertEquals(2, ordenador.getViewRowCount());
        ordenador.setRowFilter(new FiltroFuncionalidades("cadastro", false, null, fora));
        assertEquals(2, ordenador.getViewRowCount(), "busca não traz a categoria excluída");
        ordenador.setRowFilter(new FiltroFuncionalidades("", true, null, fora));
        assertEquals(1, ordenador.getViewRowCount());
        ordenador.setRowFilter(new FiltroFuncionalidades("", false, "Segurança", fora));
        assertEquals(0, ordenador.getViewRowCount(), "nem escolhendo a categoria");
    }
}
