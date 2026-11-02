package com.br.estimativadeprojetodesoftware.view.tema;

import com.formdev.flatlaf.FlatLightLaf;

/**
 * Tema claro do sistema (Strategy de aparência). As personalizações ficam em
 * TemaClaro.properties, carregado automaticamente pelo FlatLaf.
 */
public class TemaClaro extends FlatLightLaf {

    public static final String NOME = "Estimativa Claro";

    public static boolean setup() {
        return setup(new TemaClaro());
    }

    @Override
    public String getName() {
        return NOME;
    }
}
