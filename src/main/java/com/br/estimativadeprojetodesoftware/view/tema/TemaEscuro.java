package com.br.estimativadeprojetodesoftware.view.tema;

import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Tema escuro do sistema (Strategy de aparência). As personalizações ficam em
 * TemaEscuro.properties, carregado automaticamente pelo FlatLaf.
 */
public class TemaEscuro extends FlatDarkLaf {

    public static final String NOME = "Estimativa Escuro";

    public static boolean setup() {
        return setup(new TemaEscuro());
    }

    @Override
    public String getName() {
        return NOME;
    }

    @Override
    public boolean isDark() {
        return true;
    }
}
