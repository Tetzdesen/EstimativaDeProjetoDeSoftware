package com.br.estimativadeprojetodesoftware.strategy;

import com.br.estimativadeprojetodesoftware.command.database.CriarTabelasH2DatabaseCommand;
import com.br.estimativadeprojetodesoftware.command.seeder.CriarCamposSeederCommand;
import com.br.estimativadeprojetodesoftware.command.seeder.CriarUsuarioSeederCommand;
import com.br.estimativadeprojetodesoftware.command.seeder.SeederCommand;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author tetzner
 */
public class H2Initializer implements DatabaseInitializer {

    @Override
    public void inicializarBanco(String url, String user, String senha) {
        configurarBancoH2();
    }

    private void configurarBancoH2() {
        new CriarTabelasH2DatabaseCommand().execute();
        List<SeederCommand> comandosSeeder = Arrays.asList(new CriarUsuarioSeederCommand(), new CriarCamposSeederCommand());
        comandosSeeder.forEach(seeder -> seeder.execute());
    }
}
