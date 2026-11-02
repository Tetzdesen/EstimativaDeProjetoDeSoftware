package com.br.estimativadeprojetodesoftware.strategy;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteInitializer implements DatabaseInitializer {

    @Override
    public String getDriver() {
        return "org.sqlite.JDBC";
    }

    @Override
    public void prepararArmazenamento(String url) {
        String caminho = url.replace("jdbc:sqlite:", "");
        if (caminho.isBlank() || caminho.startsWith(":memory:")) {
            return;
        }
        File diretorio = new File(caminho).getAbsoluteFile().getParentFile();
        if (diretorio != null && !diretorio.exists() && !diretorio.mkdirs()) {
            throw new IllegalStateException("Não foi possível criar a pasta do banco: " + diretorio);
        }
    }

    @Override
    public void configurarConexao(Connection conexao) throws SQLException {
        try (Statement st = conexao.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
    }
}
