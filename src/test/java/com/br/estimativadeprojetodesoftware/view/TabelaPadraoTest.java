package com.br.estimativadeprojetodesoftware.view;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.view.componentes.TabelaPadrao;
import com.br.estimativadeprojetodesoftware.view.tema.TemaClaro;
import java.awt.Font;
import java.math.BigDecimal;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
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
}
