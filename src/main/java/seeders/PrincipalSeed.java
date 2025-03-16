package seeders;

import java.sql.Connection;

import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;

public class PrincipalSeed {
    public static void main(String[] args) {
        Connection conexao = ConexaoSingleton.getInstancia().getConexao();

        ConstrutorDeSeederService seeder = new ConstrutorDeSeederService();
        seeder.executarSeeders(conexao);

    }
}
