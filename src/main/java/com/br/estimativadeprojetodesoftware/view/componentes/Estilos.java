package com.br.estimativadeprojetodesoftware.view.componentes;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JComponent;

/**
 * Estilos do FlatLaf usados pelos componentes. As cores referenciam chaves do
 * tema ($Estimativa.*), então são recalculadas quando o tema muda.
 */
public final class Estilos {

    public static final String TITULO = "font: +3 bold; foreground: $Estimativa.titulo";
    public static final String SUBTITULO = "foreground: $Estimativa.subtitulo";
    public static final String ROTULO_PEQUENO = "font: -1; foreground: $Estimativa.subtitulo";
    public static final String VALOR_GRANDE = "font: +6 bold; foreground: $Estimativa.titulo";
    public static final String VALOR_DESTAQUE = "font: +6 bold; foreground: $Estimativa.destaque";
    public static final String AVISO_ESTIMATIVA = "font: -1 bold; foreground: $Estimativa.aviso";
    public static final String ERRO_CAMPO = "font: -1; foreground: $Estimativa.erro";
    public static final String SECAO = "font: +1 bold; foreground: $Estimativa.titulo";
    public static final String BOTAO_PRINCIPAL = "background: $Estimativa.botaoPrincipal; foreground: $Estimativa.botaoPrincipalTexto;"
            + "borderColor: $Estimativa.botaoPrincipal; focusedBorderColor: $Estimativa.botaoPrincipalPressionado;"
            + "hoverBackground: $Estimativa.botaoPrincipalHover; pressedBackground: $Estimativa.botaoPrincipalPressionado;"
            + "disabledBackground: $Button.disabledBackground; disabledText: $Button.disabledText;"
            + "disabledBorderColor: $Button.disabledBorderColor;"
            + "font: bold";
    public static final String BOTAO_PERIGO = "foreground: $Estimativa.erro";
    public static final String CARTAO = "arc: 12; background: $Estimativa.cartao;"
            + "border: 12,16,12,16,$Estimativa.cartaoBorda,1,12";
    public static final String CARTAO_DESTAQUE = "arc: 12; background: $Estimativa.cartao;"
            + "border: 12,16,12,16,$Estimativa.destaque,2,12";

    /** Faixa de resumo no rodapé da tela de projeto: linha divisória só em cima. */
    public static final String BARRA_RESUMO = "background: $Estimativa.cartao;"
            + "border: 10,16,10,16,$Estimativa.cartaoBorda,1,0";
    public static final String VALOR_RESUMO = "font: +3 bold; foreground: $Estimativa.titulo";
    public static final String VALOR_RESUMO_DESTAQUE = "font: +5 bold; foreground: $Estimativa.destaque";

    private Estilos() {
    }

    public static <T extends JComponent> T aplicar(T componente, String estilo) {
        componente.putClientProperty(FlatClientProperties.STYLE, estilo);
        return componente;
    }
}
