package com.br.estimativadeprojetodesoftware.strategy;

import com.br.estimativadeprojetodesoftware.command.database.CriarTabelasSQLiteDatabaseCommand;
import com.br.estimativadeprojetodesoftware.command.seeder.CriarCamposSeederCommand;
import com.br.estimativadeprojetodesoftware.command.seeder.CriarUsuarioSeederCommand;
import com.br.estimativadeprojetodesoftware.command.seeder.SeederCommand;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author tetzner
 */
public class SQLiteInitializer implements DatabaseInitializer {

    @Override
    public void inicializarBanco(String url, String user, String senha) {

        String caminhoBanco = url.replace("jdbc:sqlite:", "");
        boolean arquivoCriado = criarArquivoBDSQLite(caminhoBanco);

        if (arquivoCriado) {
            configurarBancoSQLite();
        }
    }

    private boolean criarArquivoBDSQLite(String caminho) {
        File arquivo = new File(caminho);
        if (!arquivo.exists()) {
            try {
                return arquivo.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException("Erro ao criar arquivo SQLite: " + e.getMessage());
            }
        }
        return false;
    }

    private void configurarBancoSQLite() {
        new CriarTabelasSQLiteDatabaseCommand().execute();
        // List<SeederCommand> comandosSeeder = Arrays.asList(new CriarUsuarioSeederCommand(), new CriarCamposSeederCommand());
        // comandosSeeder.forEach(seeder -> seeder.execute());
    }

}
