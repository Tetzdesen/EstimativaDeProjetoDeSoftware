package com.github.tetzdesen.estimativalog;

public enum FormatoLog {
    CSV("csv"),
    JSON("json");

    private final String extensao;

    FormatoLog(String extensao) {
        this.extensao = extensao;
    }

    public String getExtensao() {
        return extensao;
    }

    public static FormatoLog deTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Formato de log vazio");
        }
        return FormatoLog.valueOf(texto.trim().toUpperCase());
    }
}
