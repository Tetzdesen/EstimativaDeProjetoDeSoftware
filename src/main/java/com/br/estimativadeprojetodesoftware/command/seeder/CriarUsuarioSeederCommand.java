package com.br.estimativadeprojetodesoftware.command.seeder;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author tetzner
 */
public class CriarUsuarioSeederCommand implements Command {

    private final Connection conexao = ConexaoSingleton.getInstancia().getConexao();

    @Override
    public void execute() {
        String sqlUsuario = """
              INSERT INTO usuario (idUsuario, nomeUsuario, email, senha, log) VALUES (?, ?, ?, ?, ?);
          """;
          
        try (PreparedStatement stmt = conexao.prepareStatement(sqlUsuario)) {
            stmt.setString(1, "533ee667-86a2-47c7-8f99-b8fb18f19fd8");
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
