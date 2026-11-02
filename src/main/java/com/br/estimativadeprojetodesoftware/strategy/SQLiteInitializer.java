package com.br.estimativadeprojetodesoftware.strategy;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.command.database.CriarTabelasSQLiteCommand;
import com.br.estimativadeprojetodesoftware.command.seeder.CriarCamposSeederCommand;
import com.br.estimativadeprojetodesoftware.command.seeder.CriarUsuarioSeederCommand;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author tetzner
 */
public class SQLiteInitializer implements DatabaseInitializer {

    @Override
    public boolean prepararArmazenamento(String url, String user, String senha) {
        String caminhoBanco = url.replace("jdbc:sqlite:", "");
        return criarArquivoBDSQLite(caminhoBanco);
    }

    @Override
    public void configurarBanco() {
        new CriarTabelasSQLiteCommand().execute();
        List<Command> comandosSeeder = Arrays.asList(new CriarUsuarioSeederCommand(), new CriarCamposSeederCommand());
        comandosSeeder.forEach(seeder -> seeder.execute());
    }

    private boolean criarArquivoBDSQLite(String caminho) {

        File arquivo = new File(caminho);
        File diretorio = arquivo.getParentFile(); 

        if (diretorio != null && !diretorio.exists()) {
            if (!diretorio.mkdirs()) {
                throw new RuntimeException("Erro ao criar diretório: " + diretorio.getAbsolutePath());
            }
        }

        if (!arquivo.exists()) {
            try {
                return arquivo.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException("Erro ao criar arquivo SQLite: " + e.getMessage());
            }
        }
        return false;
    }

}
