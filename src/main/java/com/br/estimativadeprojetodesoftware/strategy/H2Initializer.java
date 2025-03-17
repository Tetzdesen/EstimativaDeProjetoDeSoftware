package com.br.estimativadeprojetodesoftware.strategy;

import com.br.estimativadeprojetodesoftware.command.database.CriarTabelasH2Command;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.tools.Server;

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
        try {
            // Inicia o console do H2
            Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        } catch (SQLException ex) {
            Logger.getLogger(H2Initializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("Console do H2 disponível em: http://localhost:8082");
        //    try {
        // Server.main("-web", "-webAllowOthers", "-webPort", "8082");
        // } catch (SQLException e) {
        // throw new RuntimeException("Não foi possível iniciar o servidor H2", e);
        //   }
        new CriarTabelasH2Command().execute();
        // List<SeederCommand> comandosSeeder = Arrays.asList(new CriarUsuarioSeederCommand(), new CriarCamposSeederCommand());
        // comandosSeeder.forEach(seeder -> seeder.execute());
    }
}
