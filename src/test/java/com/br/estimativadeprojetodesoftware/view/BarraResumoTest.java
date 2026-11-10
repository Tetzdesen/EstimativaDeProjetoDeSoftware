package com.br.estimativadeprojetodesoftware.view;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.view.componentes.BarraResumo;
import com.br.estimativadeprojetodesoftware.view.tema.TemaClaro;
import java.awt.Component;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** A barra de resumo mostra os quatro indicadores também numa janela estreita (1024×600). */
class BarraResumoTest {

    @BeforeAll
    static void tema() {
        TemaClaro.setup();
    }

    private static BarraResumo barra() {
        BarraResumo b = new BarraResumo("Preço final ao cliente", "Prazo", "Custo de desenvolvimento", "Média mensal");
        b.getIndicador(0).setValor("R$ 193.276,20");
        b.getIndicador(0).setDetalhe("impostos 42,00% · lucro 30,00%");
        b.getIndicador(1).setValor("216 dias");
        b.getIndicador(1).setDetalhe("7,2 meses");
        b.getIndicador(2).setValor("R$ 92.700,00");
        b.getIndicador(2).setDetalhe("216,00 dias de trabalho");
        b.getIndicador(3).setValor("R$ 26.843,92");
        return b;
    }

    /** Lado direito do indicador, nas coordenadas da barra. */
    private static int direita(BarraResumo b, Component indicador) {
        return SwingUtilities.convertPoint(indicador.getParent(), indicador.getX() + indicador.getWidth(), 0, b).x;
    }

    private static void dispor(BarraResumo b, int largura) {
        b.setSize(largura, 120);
        b.doLayout();
        b.validate();
        b.setSize(largura, b.getPreferredSize().height);
        b.validate();
    }

    @Test
    void larguraFolgadaMantemDetalheAoLadoDoValor() {
        BarraResumo b = barra();
        dispor(b, 1100);
        assertFalse(b.getIndicador(0).isCompacto());
    }

    @Test
    void janelaEstreitaPassaParaCompactoEMostraOUltimoIndicador() {
        BarraResumo b = barra();
        dispor(b, 714); // área da barra na janela de projeto em 1024×600
        assertTrue(b.getIndicador(0).isCompacto());
        assertTrue(direita(b, b.getIndicador(3)) <= b.getWidth(), "Média mensal fica dentro da barra");

        dispor(b, 1100);
        assertFalse(b.getIndicador(0).isCompacto(), "voltando a alargar, o detalhe volta para o lado");
    }
}
