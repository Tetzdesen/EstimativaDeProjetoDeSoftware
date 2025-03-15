package com.br.estimativadeprojetodesoftware.command.seeder;

import com.br.estimativadeprojetodesoftware.builder.AndroidBuilder;
import com.br.estimativadeprojetodesoftware.builder.Diretor;
import com.br.estimativadeprojetodesoftware.builder.IosBuilder;
import com.br.estimativadeprojetodesoftware.builder.WebBackEndBuilder;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author tetzner
 */

/*
public class CriarPerfisSeederCommand implements SeederCommand {

    @Override
    public void execute() {
        Connection conexao = ConexaoSingleton.getInstancia().getConexao();
        String sql = "INSERT INTO perfil (idPerfil, nomePerfil, perfilBackend, created_atPerfil, usuario_idUsuario) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setString(1, "");
            statement.setString(2, "Android");
            statement.setBoolean(3, 0);
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(5, "");

            statement.executeUpdate();

            List<Campo> campos;

            campos = new CampoRepositoryService().listarTodosPorTipo("tamanho");

            for (Campo campo : campos) {
                campo.setDias(perfil.getTamanhosApp().get(campo.getNome()).doubleValue());
                new CampoRepositoryService().salvarPerfilCampo(perfil, campo);
            }

            campos = new CampoRepositoryService().listarTodosPorTipo("nivel");

            for (Campo campo : campos) {
                campo.setDias(perfil.getNiveisUI().get(campo.getNome()));
                new CampoRepositoryService().salvarPerfilCampo(perfil, campo);
            }

            campos = new CampoRepositoryService().listarTodosPorTipo("funcionalidade");

            // lembrar de colocar quando é uma funcionalidade nova
            for (Campo campo : campos) {
                if (perfil.getFuncionalidades().containsKey(campo.getNome()) && campo.getTipo().equalsIgnoreCase("funcionalidade")) {
                    campo.setDias(perfil.getFuncionalidades().get(campo.getNome()).doubleValue());
                    new CampoRepositoryService().salvarPerfilCampo(perfil, campo);
                }
            }

            campos = new CampoRepositoryService().listarTodosPorTipo("taxa diária");
            for (Campo campo : campos) {
                campo.setDias(perfil.getTaxasDiarias().get(campo.getNome()));
                new CampoRepositoryService().salvarPerfilCampo(perfil, campo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

*/
