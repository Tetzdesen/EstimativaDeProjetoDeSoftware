package com.br.estimativadeprojetodesoftware.repository.sqlite;

import com.br.estimativadeprojetodesoftware.chain.carregarcampos.EmpilhadorDeCampoPerfilService;
import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;
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
    public void salvar(PerfilProjeto perfil) {
        String sql = "INSERT INTO perfil (idPerfil, nomePerfil, perfilBackend, created_atPerfil, usuario_idUsuario) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, perfil.getId().toString());
            statement.setString(2, perfil.getNome());
            statement.setBoolean(3, perfil.isPerfilBackEnd());
            statement.setTimestamp(4, Timestamp.valueOf(perfil.getCreated_at()));
            statement.setString(5, perfil.getUsuario().getId().toString());

            statement.executeUpdate();

            CampoRepositoryService campoService = new CampoRepositoryService();
            List<Campo> campos;

            campos = campoService.listarTodosPorTipo("tamanho");

            for (Campo campo : campos) {
                campo.setDias(perfil.getTamanhosApp().get(campo.getNome()).doubleValue());
                campoService.salvarPerfilCampo(perfil, campo);
            }

            campos = campoService.listarTodosPorTipo("nivel");

            for (Campo campo : campos) {
                campo.setDias(perfil.getNiveisUI().get(campo.getNome()));
                campoService.salvarPerfilCampo(perfil, campo);
            }

            campos = campoService.listarTodosPorTipo("funcionalidade");
            /*
            for (Campo campo : campos) {
                if (perfil.getFuncionalidades().containsKey(campo.getNome()) && campo.getTipo().equalsIgnoreCase("funcionalidade")) {
                    campo.setDias(perfil.getFuncionalidades().get(campo.getNome()).doubleValue());
                    long start = System.currentTimeMillis();
                    logStart("PerfilRepositorySQLite.salvar: funcionalidades");
                    //a lerdeza está aqui
                    campoService.salvarPerfilCampo(perfil, campo);
                    logEnd("PerfilRepositorySQLite.salvar: funcionalidades", start);
                }
            }
             */
            List<Campo> camposParaSalvar = new ArrayList<>();
            for (Campo campo : campos) {
                if (perfil.getFuncionalidades().containsKey(campo.getNome())
                        && campo.getTipo().equalsIgnoreCase("funcionalidade")) {
                    campo.setDias(perfil.getFuncionalidades().get(campo.getNome()).doubleValue());
                    camposParaSalvar.add(campo);
                }
            }
            campoService.salvarPerfilCampos(perfil, camposParaSalvar);

            campos = campoService.listarTodosPorTipo("taxa diária");
            for (Campo campo : campos) {
                campo.setDias(perfil.getTaxasDiarias().get(campo.getNome()));
                campoService.salvarPerfilCampo(perfil, campo);
            }

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
            statement.setString(3, perfil.getUsuario().getId().toString());
            statement.setString(4, perfil.getId().toString());

            CampoRepositoryService campoService = new CampoRepositoryService();
            List<Campo> campos;

            campoService.removerPorIdPerfil(perfil.getId());

            campos = campoService.listarTodosPorTipo("tamanho");

            for (Campo campo : campos) {
                campo.setDias(perfil.getTamanhosApp().get(campo.getNome()).doubleValue());
                campoService.salvarPerfilCampo(perfil, campo);
            }

            campos = campoService.listarTodosPorTipo("nivel");

            for (Campo campo : campos) {
                campo.setDias(perfil.getNiveisUI().get(campo.getNome()));
                campoService.salvarPerfilCampo(perfil, campo);
            }

            for (Entry<String, Integer> entry : perfil.getFuncionalidades().entrySet()) {
                Campo campo = campoService.buscarPorNome(entry.getKey());
                if (campo == null) {
                    campo = new Campo("funcionalidade", entry.getKey(), entry.getValue().doubleValue());
                    campoService.salvar(campo);
                }

                campo.setDias(perfil.getFuncionalidades().get(campo.getNome()).doubleValue());
                campoService.salvarPerfilCampo(perfil, campo);
            }

            campos = campoService.listarTodosPorTipo("taxa diária");

            for (Campo campo : campos) {
                campo.setDias(perfil.getTaxasDiarias().get(campo.getNome()));
                campoService.salvarPerfilCampo(perfil, campo);
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
                perfis.add(buscarPorId(UUID.fromString(rs.getString("perfil_idPerfil"))).get());
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
    public List<PerfilProjeto> buscarTodosPerfisPorIdUsuario(UUID id) {
        Set<UUID> perfisIds = new HashSet<>();
        List<PerfilProjeto> perfis = new ArrayList<>();

        String sql = "SELECT * FROM perfil WHERE usuario_idUsuario = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UUID idPerfil = UUID.fromString(resultSet.getString("idPerfil"));

                // Evita perfis duplicados
                if (!perfisIds.contains(idPerfil)) {
                    PerfilProjeto perfil = mapToPerfil(resultSet);
                    perfis.add(perfil);
                    perfisIds.add(idPerfil);
                }
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
                UsuarioLogadoSingleton.getInstancia().getUsuario()
        );

        EmpilhadorDeCampoPerfilService empilhador = new EmpilhadorDeCampoPerfilService();
        empilhador.carregarCampos(perfil);

        return perfil;
    }

    @Override
    public int obterQuantidadePerfisPorUsuario(UUID idUsuario) {
        String sql = "SELECT COUNT(*) FROM perfil WHERE usuario_idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, idUsuario.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter a quantidade de perfis do usuário", e);
        }
        return 0; 
    }
}
