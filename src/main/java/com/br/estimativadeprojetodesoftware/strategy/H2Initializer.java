package com.br.estimativadeprojetodesoftware.strategy;

import java.io.File;

/** H2 embutido: cria a pasta do arquivo; chaves estrangeiras já vêm ativas. */
public class H2Initializer implements DatabaseInitializer {

    @Override
    public String getDriver() {
        return "org.h2.Driver";
    }

    @Override
    public void prepararArmazenamento(String url) {
        String resto = url.replace("jdbc:h2:", "");
        if (resto.startsWith("mem:") || resto.startsWith("tcp:") || resto.startsWith("ssl:")) {
            return;
        }
        String caminho = resto.replaceFirst("^file:", "").split(";")[0];
        File diretorio = new File(caminho).getAbsoluteFile().getParentFile();
        if (diretorio != null && !diretorio.exists() && !diretorio.mkdirs()) {
            throw new IllegalStateException("Não foi possível criar a pasta do banco: " + diretorio);
        }
    }
}
