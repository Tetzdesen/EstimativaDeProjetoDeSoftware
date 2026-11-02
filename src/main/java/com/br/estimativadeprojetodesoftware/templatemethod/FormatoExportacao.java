package com.br.estimativadeprojetodesoftware.templatemethod;

public enum FormatoExportacao {
    PDF("pdf"),
    CSV("csv");

    private final String extensao;

    FormatoExportacao(String extensao) {
        this.extensao = extensao;
    }

    public String getExtensao() {
        return extensao;
    }
}
