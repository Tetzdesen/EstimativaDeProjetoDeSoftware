package com.br.estimativadeprojetodesoftware.repository.sqlite;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PerfilRepositorySQLite implements IPerfilRepository {

    private final Connection connection;

    public PerfilRepositorySQLite() {
        this.connection = ConexaoSingleton.getInstancia().getConexao();
    }

    @Override
    public void salvar(Perfil perfil) {
        String sql = "INSERT INTO perfil (idPerfil, nomePerfil, perfilBackend, created_atPerfil, usuario_idUsuario) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, perfil.getId().toString());
            statement.setString(2, perfil.getNome());
            statement.setBoolean(3, perfil.isPerfilBackEnd());
            statement.setTimestamp(4, Timestamp.valueOf(perfil.getCreated_at()));
            statement.setString(5, perfil.getUsuario().getId().toString());

            for (Map.Entry<String, Integer> entry : perfil.getTamanhosApp().entrySet()) {
                CampoRepositoryService.getInstancia().salvar(new Campo(UUID.randomUUID(), "tamanho app", entry.getKey(), entry.getValue().doubleValue()));
            }

            for (Map.Entry<String, Double> entry : perfil.getNiveisUI().entrySet()) {
                CampoRepositoryService.getInstancia().salvar(new Campo(UUID.randomUUID(), "nivel ui", entry.getKey(), entry.getValue()));
            }

            for (Map.Entry<String, Integer> entry : perfil.getFuncionalidades().entrySet()) {
                CampoRepositoryService.getInstancia().salvar(new Campo(UUID.randomUUID(), "funcionalidades", entry.getKey(), entry.getValue().doubleValue()));
            }

            for (Map.Entry<String, Double> entry : perfil.getTaxasDiarias().entrySet()) {
                CampoRepositoryService.getInstancia().salvar(new Campo(UUID.randomUUID(), "taxa diaria", entry.getKey(), entry.getValue()));
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Perfil perfil
    ) {
        String sql = "UPDATE perfil SET nomePerfil = ?, perfilBackend = ?, updated_atPerfil = CURRENT_TIMESTAMP, usuario_idUsuario = ? WHERE idPerfil = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, perfil.getNome());
            statement.setBoolean(2, perfil.isPerfilBackEnd());
            statement.setString(4, perfil.getUsuario().getId().toString());
            statement.setString(5, perfil.getId().toString());

            for (Map.Entry<String, Integer> entry : perfil.getTamanhosApp().entrySet()) {
                CampoRepositoryService.getInstancia().atualizar(new Campo(UUID.randomUUID(), "tamanho app", entry.getKey(), entry.getValue().doubleValue()));
            }

            for (Map.Entry<String, Double> entry : perfil.getNiveisUI().entrySet()) {
                CampoRepositoryService.getInstancia().atualizar(new Campo(UUID.randomUUID(), "nivel ui", entry.getKey(), entry.getValue()));
            }

            for (Map.Entry<String, Integer> entry : perfil.getFuncionalidades().entrySet()) {
                CampoRepositoryService.getInstancia().atualizar(new Campo(UUID.randomUUID(), "funcionalidades", entry.getKey(), entry.getValue().doubleValue()));
            }

            for (Map.Entry<String, Double> entry : perfil.getTaxasDiarias().entrySet()) {
                CampoRepositoryService.getInstancia().atualizar(new Campo(UUID.randomUUID(), "taxa diaria", entry.getKey(), entry.getValue()));
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removerPorId(UUID id
    ) {
        String sql = "DELETE FROM perfil WHERE idPerfil = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Perfil> buscarPorId(UUID id
    ) {
        String sql = "SELECT * FROM perfil WHERE idPerfil = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToPerfil(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Perfil> buscarTodosPerfisPorIdUsuario(UUID id
    ) {
        List<Perfil> perfis = new ArrayList<>();
        String sql = "SELECT * FROM perfil WHERE usuario_idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                perfis.add(mapToPerfil(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return perfis;
    }

    @Override
    public List<Perfil> buscarPerfisPorProjeto(UUID projetoId
    ) {
        List<Perfil> perfis = new ArrayList<>();
        String sql = "SELECT perfil_idPerfil FROM projeto_has_perfil WHERE projeto_idProjeto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projetoId.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                perfis.add(PerfilRepositoryService.getInstancia().buscarPorId(UUID.fromString(rs.getString("perfil_idPerfil"))).get());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar perfis do projeto", e);
        }
        return perfis;
    }

    @Override
    public List<Perfil> buscarTodos() {
        List<Perfil> perfis = new ArrayList<>();
        String sql = "SELECT * FROM perfil";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                perfis.add(mapToPerfil(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return perfis;
    }

    private Perfil mapToPerfil(ResultSet resultSet) throws SQLException {

        UUID idPerfil = UUID.fromString(resultSet.getString("idPerfil"));

        Perfil perfil = new Perfil(
                idPerfil,
                resultSet.getString("nomePerfil"),
                resultSet.getBoolean("perfilBackend"),
                resultSet.getTimestamp("created_atPerfil").toLocalDateTime(),
                UsuarioRepositoryService.getInstancia().buscarPorId(UUID.fromString(resultSet.getString("usuario_idUsuario"))).get()
        );

        // buscar nome do campo pelo id do Perfil
        List<Campo> camposTamanhoApp = CampoRepositoryService.getInstancia().buscarPorIdPerfilTipo(perfil.getId(), "tamanho app");

        for (Campo campo : camposTamanhoApp) {
            Double dias = CampoRepositoryService.getInstancia().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
            perfil.adicionarTamanhoApp(campo.getNome(), dias.intValue());

        }

        List<Campo> camposNivelUI = CampoRepositoryService.getInstancia().buscarPorIdPerfilTipo(perfil.getId(), "nivel ui");

        for (Campo campo : camposNivelUI) {
            Double dias = CampoRepositoryService.getInstancia().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
            perfil.adicionarNivelUI(campo.getNome(), dias.intValue());
        }

        List<Campo> camposFuncionalidades = CampoRepositoryService.getInstancia().buscarPorIdPerfilTipo(perfil.getId(), "funcionalidades");

        for (Campo campo : camposFuncionalidades) {
            Double dias = CampoRepositoryService.getInstancia().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
            perfil.adicionarFuncionalidade(campo.getNome(), dias.intValue());
        }

        List<Campo> taxasDiarias = CampoRepositoryService.getInstancia().buscarPorIdPerfilTipo(perfil.getId(), "taxa diaria");

        for (Campo campo : taxasDiarias) {
            Double dias = CampoRepositoryService.getInstancia().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
            perfil.adicionarTaxaDiaria(campo.getNome(), dias.intValue());
        }

        return perfil;
    }
}
