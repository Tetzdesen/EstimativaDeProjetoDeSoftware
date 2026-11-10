package com.br.estimativadeprojetodesoftware.view;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.view.componentes.TabelaPadrao;
import com.br.estimativadeprojetodesoftware.view.tema.TemaClaro;
import java.awt.Font;
import java.math.BigDecimal;
import javax.swing.JLabel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Renderizadores da tabela padrão: "NA" distinto de "0", números à direita, reais formatados. */
class TabelaPadraoTest {

    private static TabelaPadrao tabela;

    @BeforeAll
    static void preparar() {
        TemaClaro.setup();
        DefaultTableModel m = new DefaultTableModel(new Object[][]{
            {"Faturamento", "NA", "0", new BigDecimal("59033.3333")},
            {"Administração", "8", "R$ 59.033,33", new BigDecimal("0.42")}}, new Object[]{"Categoria", "iOS", "Web", "Valor"});
        tabela = new TabelaPadrao(m);
        tabela.setFormatoColuna(3, TabelaPadrao.Formato.REAIS);
    }

    private JLabel celula(int linha, int coluna) {
        return (JLabel) tabela.prepareRenderer(tabela.getCellRenderer(linha, coluna), linha, coluna);
    }

    @Test
    void naEmItalicoComCorPropriaEZeroComoNumero() {
        JLabel na = celula(0, 1);
        assertEquals("NA", na.getText());
        assertTrue(na.getFont().isItalic());
        assertEquals(UIManager.getColor("Estimativa.na"), na.getForeground());

        JLabel zero = celula(0, 2);
        assertEquals("0", zero.getText());
        assertFalse(zero.getFont().isItalic());
        assertNotEquals(UIManager.getColor("Estimativa.na"), zero.getForeground());
        assertEquals(SwingConstants.RIGHT, zero.getHorizontalAlignment());
    }

    @Test
    void reaisFormatadosEAlinhadosADireita() {
        JLabel reais = celula(0, 3);
        assertEquals("R$ 59.033,33", reais.getText());
        assertEquals(SwingConstants.RIGHT, reais.getHorizontalAlignment());
        assertEquals(SwingConstants.RIGHT, celula(1, 2).getHorizontalAlignment(), "texto já formatado em reais");
        assertEquals(Font.PLAIN, celula(1, 2).getFont().getStyle());
    }

    @Test
    void textoAEsquerdaEPercentual() {
        assertEquals(SwingConstants.LEADING, celula(0, 0).getHorizontalAlignment());
        assertEquals("42%", TabelaPadrao.formatar(new BigDecimal("0.42"), TabelaPadrao.Formato.PERCENTUAL));
        assertEquals("23,33", TabelaPadrao.formatar(new BigDecimal("23.3333"), TabelaPadrao.Formato.NUMERO));
    }

    @Test
    void destaqueUsaALinhaDoModeloMesmoComFiltro() {
        DefaultTableModel m = new DefaultTableModel(new Object[][]{{"Primeira"}, {"Segunda"}, {"Terceira"}}, new Object[]{"Nome"});
        TabelaPadrao filtrada = new TabelaPadrao(m);
        TableRowSorter<TableModel> ordenador = new TableRowSorter<>(m);
        ordenador.setRowFilter(RowFilter.regexFilter("Segunda|Terceira"));
        filtrada.setRowSorter(ordenador);
        filtrada.setDestaqueLinha(linhaDoModelo -> linhaDoModelo == 2);

        JLabel segunda = (JLabel) filtrada.prepareRenderer(filtrada.getCellRenderer(0, 0), 0, 0);
        assertEquals("Segunda", segunda.getText());
        assertFalse(segunda.getFont().isBold());
        JLabel terceira = (JLabel) filtrada.prepareRenderer(filtrada.getCellRenderer(1, 0), 1, 0);
        assertEquals("Terceira", terceira.getText());
        assertTrue(terceira.getFont().isBold());
    }

    @Test
    void naEmLinhaDestacadaContinuaItalico() {
        DefaultTableModel m = new DefaultTableModel(new Object[][]{{"NA"}}, new Object[]{"iOS"});
        TabelaPadrao destacada = new TabelaPadrao(m);
        destacada.setDestaqueLinha(linha -> true);
        JLabel na = (JLabel) destacada.prepareRenderer(destacada.getCellRenderer(0, 0), 0, 0);
        assertTrue(na.getFont().isItalic());
        assertTrue(na.getFont().isBold());
    }

    @Test
    void enumsDoPerfilAparecemComDescricaoEmVezDoNomeInterno() {
        assertEquals("Múltipla", TabelaPadrao.formatar(com.br.estimativadeprojetodesoftware.model.TipoSelecao.MULTIPLA,
                TabelaPadrao.Formato.TEXTO));
        assertEquals("Percentual", TabelaPadrao.formatar(com.br.estimativadeprojetodesoftware.model.TipoValor.PERCENTUAL,
                TabelaPadrao.Formato.TEXTO));
        // o nome gravado no banco continua o mesmo
        assertEquals("MULTIPLA", com.br.estimativadeprojetodesoftware.model.TipoSelecao.MULTIPLA.toString());
    }

    @Test
    void janelaEstreitaRolaNaHorizontalEmVezDeEsconderColunas() {
        DefaultTableModel m = new DefaultTableModel(new Object[]{"A", "B"}, 1);
        TabelaPadrao t = new TabelaPadrao(m);
        t.getColumnModel().getColumn(0).setMinWidth(200);
        t.getColumnModel().getColumn(1).setMinWidth(200);
        javax.swing.JScrollPane rolagem = new javax.swing.JScrollPane(t);
        rolagem.getViewport().setSize(300, 100);
        assertFalse(t.getScrollableTracksViewportWidth());
        rolagem.getViewport().setSize(500, 100);
        assertTrue(t.getScrollableTracksViewportWidth());
    }
}
