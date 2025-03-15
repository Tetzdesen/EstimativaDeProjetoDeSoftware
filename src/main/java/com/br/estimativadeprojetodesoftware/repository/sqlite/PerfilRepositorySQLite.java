package com.br.estimativadeprojetodesoftware.repository.sqlite;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

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

    @Override
    public void atualizar(Perfil perfil) {
        String sql = "UPDATE perfil SET nomePerfil = ?, perfilBackend = ?, updated_atPerfil = CURRENT_TIMESTAMP, usuario_idUsuario = ? WHERE idPerfil = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, perfil.getNome());
            statement.setBoolean(2, perfil.isPerfilBackEnd());
            statement.setString(3, perfil.getUsuario().getId().toString());
            statement.setString(4, perfil.getId().toString());

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

            for (Entry<String, Integer> entry : perfil.getFuncionalidades().entrySet()) {
                Campo campo = new CampoRepositoryService().buscarPorNome(entry.getKey());
                if (campo == null) {
                    campo = new Campo("funcionalidade", entry.getKey(), entry.getValue().doubleValue());
                    new CampoRepositoryService().salvar(campo);
                }

                if (!new CampoRepositoryService().isCampoInPerfil(perfil.getId(), campo.getId())) {
                    campo.setDias(perfil.getFuncionalidades().get(campo.getNome()).doubleValue());
                    new CampoRepositoryService().salvarPerfilCampo(perfil, campo);
                }

                campo.setDias(perfil.getFuncionalidades().get(campo.getNome()).doubleValue());
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
    public List<Perfil> buscarPerfisPorProjeto(UUID projetoId
    ) {
        List<Perfil> perfis = new ArrayList<>();
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

    @Override
    public List<Perfil> buscarTodosPerfisPorIdUsuario(UUID id) {
        Set<UUID> perfisIds = new HashSet<>();
        List<Perfil> perfis = new ArrayList<>();

        String sql = "SELECT * FROM perfil WHERE usuario_idUsuario = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UUID idPerfil = UUID.fromString(resultSet.getString("idPerfil"));

                // Evita perfis duplicados
                if (!perfisIds.contains(idPerfil)) {
                    Perfil perfil = mapToPerfil(resultSet);
                    perfis.add(perfil);
                    perfisIds.add(idPerfil);
                }
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
                UsuarioLogadoSingleton.getInstancia().getUsuario()
        );

        CampoRepositoryService campoService = new CampoRepositoryService();

        carregarCampos(perfil, "tamanho", campoService);
        carregarCampos(perfil, "nivel", campoService);
        carregarCampos(perfil, "funcionalidade", campoService);
        carregarCampos(perfil, "taxa diária", campoService);

        return perfil;
    }

    private void carregarCampos(Perfil perfil, String tipoCampo, CampoRepositoryService campoService) throws SQLException {
        List<Campo> campos = campoService.buscarPorIdPerfilTipo(perfil.getId(), tipoCampo);
        for (Campo campo : campos) {
            Double dias = campoService.buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());

            switch (tipoCampo) {
                case "tamanho" ->
                    perfil.adicionarTamanhoApp(campo.getNome(), dias.intValue());
                case "nivel" ->
                    perfil.adicionarNivelUI(campo.getNome(), dias.doubleValue());
                case "funcionalidade" ->
                    perfil.adicionarFuncionalidade(campo.getNome(), dias.intValue());
                case "taxa diária" ->
                    perfil.adicionarTaxaDiaria(campo.getNome(), dias.doubleValue());
            }
        }
    }
}
