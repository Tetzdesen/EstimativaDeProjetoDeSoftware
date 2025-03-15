package com.br.estimativadeprojetodesoftware.command.seeder;

import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class CriarUsuarioSeederCommand implements SeederCommand {

    private final Connection conexao = ConexaoSingleton.getInstancia().getConexao();

    @Override
    public void execute() {
        UUID idUsuario = UUID.randomUUID();
        // Inserção do usuário
        String sqlUsuario = """
              INSERT INTO usuario (idUsuario, nomeUsuario, email, senha, log) VALUES (?, ?, ?, ?, ?);
          """;
          
        try (PreparedStatement stmt = conexao.prepareStatement(sqlUsuario)) {
            stmt.setString(1, idUsuario.toString());
            stmt.setString(2, "kaua");
            stmt.setString(3, "kaua@email.com");
            stmt.setString(4, "A@#bb");
            stmt.setString(5, "JSON");
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
