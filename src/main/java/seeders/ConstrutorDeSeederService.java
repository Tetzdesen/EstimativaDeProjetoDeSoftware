package seeders;

import java.sql.Connection;
import java.util.List;

public class ConstrutorDeSeederService {

    private List<ITipoSeeder> seeders;

    public ConstrutorDeSeederService() {
        this.seeders = List.of(
                new AdminSeeder(),
                new ProjetoSeeder()
        );
    }

    public void executarSeeders(Connection conexao) {
        seeders.forEach(tipo -> tipo.seed(conexao));
    }
}
