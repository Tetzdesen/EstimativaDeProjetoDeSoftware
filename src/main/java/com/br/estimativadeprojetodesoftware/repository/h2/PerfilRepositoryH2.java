package com.br.estimativadeprojetodesoftware.repository.h2;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
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

public class PerfilRepositoryH2 implements IPerfilRepository {

    private final Connection connection;

    public PerfilRepositoryH2() {
        this.connection = ConexaoSingleton.getInstancia().getConexao();
    }

    @Override
    public void salvar(PerfilProjeto perfil) {
        String sql = "INSERT INTO perfil (idPerfil, nomePerfil, perfilBackend, created_atPerfil, usuario_idUsuario) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, perfil.getId().toString());
            statement.setString(2, perfil.getNome());
            statement.setBoolean(3, perfil.isPerfilBackEnd());
            statement.setTimestamp(4, Timestamp.valueOf(perfil.getCreated_at()));
            statement.setString(5, perfil.getUsuario().getId().toString());

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

            for (Campo campo : campos) {
                campo.setDias(perfil.getFuncionalidades().get(campo.getNome()).doubleValue());
                if (!perfil.getFuncionalidades().containsKey(campo.getNome()) && campo.getTipo().equalsIgnoreCase("funcionalidade")) {
                    new CampoRepositoryService().salvar(campo);
                    new CampoRepositoryService().salvarPerfilCampo(perfil, campo);
                }
                new CampoRepositoryService().salvarPerfilCampo(perfil, campo);
            }

            campos = new CampoRepositoryService().listarTodosPorTipo("taxa diária");
            for (Campo campo : campos) {
                campo.setDias(perfil.getTaxasDiarias().get(campo.getNome()));
                new CampoRepositoryService().salvarPerfilCampo(perfil, campo);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(PerfilProjeto perfil) {
        String sql = "UPDATE perfil SET nomePerfil = ?, perfilBackend = ?, updated_atPerfil = CURRENT_TIMESTAMP, usuario_idUsuario = ? WHERE idPerfil = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, perfil.getNome());
            statement.setBoolean(2, perfil.isPerfilBackEnd());
            statement.setString(4, perfil.getUsuario().getId().toString());
            statement.setString(5, perfil.getId().toString());

            List<Campo> campos;

            campos = new CampoRepositoryService().listarTodosPorTipo("tamanho");

            for (Campo campo : campos) {
                campo.setDias(perfil.getTamanhosApp().get(campo.getNome()).doubleValue());
                new CampoRepositoryService().atualizarDiasPerfilCampo(perfil, campo);
            }

            campos = new CampoRepositoryService().listarTodosPorTipo("nivel");

            for (Campo campo : campos) {
                campo.setDias(perfil.getNiveisUI().get(campo.getNome()));
                new CampoRepositoryService().atualizarDiasPerfilCampo(perfil, campo);
            }

            campos = new CampoRepositoryService().listarTodosPorTipo("funcionalidade");

            for (Campo campo : campos) {
                campo.setDias(perfil.getFuncionalidades().get(campo.getNome()).doubleValue());
                if (!perfil.getFuncionalidades().containsKey(campo.getNome()) && campo.getTipo().equalsIgnoreCase("funcionalidade")) {
                    new CampoRepositoryService().atualizar(campo);
                    new CampoRepositoryService().atualizarDiasPerfilCampo(perfil, campo);
                }
                new CampoRepositoryService().atualizarDiasPerfilCampo(perfil, campo);
            }

            campos = new CampoRepositoryService().listarTodosPorTipo("taxa diária");

            for (Campo campo : campos) {
                campo.setDias(perfil.getTaxasDiarias().get(campo.getNome()));
                new CampoRepositoryService().atualizarDiasPerfilCampo(perfil, campo);
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
    public Optional<PerfilProjeto> buscarPorId(UUID id
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
    public List<PerfilProjeto> buscarPerfisPorProjeto(UUID projetoId
    ) {
        List<PerfilProjeto> perfis = new ArrayList<>();
        String sql = "SELECT perfil_idPerfil FROM projeto_has_perfil WHERE projeto_idProjeto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projetoId.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                perfis.add(new PerfilRepositoryService().buscarPorId(UUID.fromString(rs.getString("perfil_idPerfil"))).get());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar perfis do projeto", e);
        }
        return perfis;
    }

    @Override
    public List<PerfilProjeto> buscarTodos() {
        List<PerfilProjeto> perfis = new ArrayList<>();
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

    @Override
    public List<PerfilProjeto> buscarTodosPerfisPorIdUsuario(UUID id
    ) {
        List<PerfilProjeto> perfis = new ArrayList<>();
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

    private PerfilProjeto mapToPerfil(ResultSet resultSet) throws SQLException {

        UUID idPerfil = UUID.fromString(resultSet.getString("idPerfil"));

        PerfilProjeto perfil = new PerfilProjeto(
                idPerfil,
                resultSet.getString("nomePerfil"),
                resultSet.getBoolean("perfilBackend"),
                resultSet.getTimestamp("created_atPerfil").toLocalDateTime(),
                //UsuarioLogadoSingleton.getInstancia().getUsuario()
                new UsuarioRepositoryService().buscarPorId(UUID.fromString(resultSet.getString("usuario_idUsuario"))).get()
        );

        // buscar nome do campo pelo id do Perfil
        List<Campo> camposTamanhoApp = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "tamanho app");

        for (Campo campo : camposTamanhoApp) {
            Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
            perfil.adicionarTamanhoApp(campo.getNome(), dias.intValue());

        }

        List<Campo> camposNivelUI = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "nivel ui");

        for (Campo campo : camposNivelUI) {
            Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
            perfil.adicionarNivelUI(campo.getNome(), dias.intValue());
        }

        List<Campo> camposFuncionalidades = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "funcionalidades");

        for (Campo campo : camposFuncionalidades) {
            Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
            perfil.adicionarFuncionalidade(campo.getNome(), dias.intValue());
        }

        List<Campo> taxasDiarias = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "taxa diaria");

        for (Campo campo : taxasDiarias) {
            Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
            perfil.adicionarTaxaDiaria(campo.getNome(), dias.intValue());
        }

        return perfil;
    }
}
