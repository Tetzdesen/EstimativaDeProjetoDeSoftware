package com.br.estimativadeprojetodesoftware.view.componentes;

import javax.swing.JLabel;

/** Selo de situação (ex.: "Calculado", "Somente leitura") com cor por tipo. */
public class Selo extends JLabel {

    public enum Tipo {
        NEUTRO("Estimativa.subtitulo"),
        DESTAQUE("Estimativa.destaque"),
        SUCESSO("Estimativa.sucesso"),
        AVISO("Estimativa.aviso"),
        ERRO("Estimativa.erro");

        private final String cor;

        Tipo(String cor) {
            this.cor = cor;
        }

        public String chaveCor() {
            return cor;
        }
    }

    public Selo() {
        exibir(" ", Tipo.NEUTRO);
    }

    public final void exibir(String texto, Tipo tipo) {
        setText(texto);
        Estilos.aplicar(this, "font: -1 bold; foreground: $" + tipo.cor + "; border: 3,10,3,10,$" + tipo.cor + ",1,999");
        setVisible(texto != null && !texto.isBlank());
    }
}
