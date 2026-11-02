package com.github.tetzdesen.estimativalog;

/**
 * Interface alvo do padrão Adapter: o sistema registra operações sem conhecer
 * o formato do arquivo. Cada formato é um adaptador que implementa esta interface.
 */
public interface Logger {

    /**
     * Grava o registro no destino do adaptador.
     *
     * @throws LogException quando não for possível gravar
     */
    void registrar(LogRegistro registro);

    FormatoLog getFormato();
}
