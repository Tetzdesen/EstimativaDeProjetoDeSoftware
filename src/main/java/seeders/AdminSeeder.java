package seeders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminSeeder implements ITipoSeeder {

    @Override
    public void seed(Connection connection) {
        String sql = """
        
            INSERT INTO usuario (idUsuario, nomeUsuario, email, senha, created_atUsuario, log) VALUES (
                '0d1f767d-46d7-401b-922b-ff1a063ce661', 'admin', 'admin@email.com', 'admin', '2000-01-01 00:00:00', 'JSON'
            )
        
        """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.execute();
            System.out.println("Admin criado com sucesso");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    
    }

}
