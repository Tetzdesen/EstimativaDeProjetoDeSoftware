package com.br.estimativadeprojetodesoftware.repository.sqlite;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.repository.ICampoRepository;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CampoRepositorySQLite implements ICampoRepository {

    private final Connection connection;

    public CampoRepositorySQLite() {
        this.connection = ConexaoSingleton.getInstancia().getConexao();
    }

    @Override
    public void salvar(Campo campo) {
        String sql = "INSERT INTO campo (idCampo, tipoCampo, nomeCampo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, campo.getId().toString());
            stmt.setString(2, campo.getTipo());
            stmt.setString(3, campo.getNome());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar campo: " + e.getMessage(), e);
        }
    }

    @Override
    public void salvarProjetoCampo(Projeto projeto, Campo campo) {
        if (!existeAssociacao(projeto.getId(), campo.getId())) {
            String sql = "INSERT INTO projeto_has_campo (projeto_idProjeto, campo_idCampo, diasProjeto) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, projeto.getId().toString());
                stmt.setString(2, campo.getId().toString());
                stmt.setInt(3, campo.getDias().intValue());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao salvar campo de projeto: " + e.getMessage(), e);
            }
        }
    }

    private boolean existeAssociacao(UUID idProjeto, UUID idCampo) {
        String sql = "SELECT COUNT(*) FROM projeto_has_campo WHERE projeto_idProjeto = ? AND campo_idCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idProjeto.toString());
            stmt.setString(2, idCampo.toString());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar associação", e);
        }
    }

    @Override
    public void salvarPerfilCampo(PerfilProjeto perfil, Campo campo) {
        String sql = "INSERT INTO perfil_has_campo (perfil_idPerfil, campo_idCampo, diasPerfil) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, perfil.getId().toString());
            stmt.setString(2, campo.getId().toString());
            stmt.setDouble(3, campo.getDias());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar campo de perfil: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Campo campo) {
        String sql = "UPDATE campo SET tipoCampo = ?, nomeCampo = ? WHERE idCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, campo.getTipo());
            stmt.setString(2, campo.getNome());
            stmt.setString(3, campo.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar campo: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizarDiasProjetoCampo(Projeto projeto, Campo campo) {
        String sql = "UPDATE projeto_has_campo SET diasProjeto = ? WHERE projeto_idProjeto = ? AND campo_idCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, campo.getDias().intValue());
            stmt.setString(2, projeto.getId().toString());
            stmt.setString(3, campo.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar campo: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizarDiasPerfilCampo(PerfilProjeto perfil, Campo campo) {
        String sql = "UPDATE perfil_has_campo SET diasPerfil = ? WHERE perfil_idPerfil = ? AND campo_idCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, campo.getDias().doubleValue());
            stmt.setString(2, perfil.getId().toString());
            stmt.setString(3, campo.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar campo: " + e.getMessage(), e);
        }
    }

    @Override
    public void removerPorID(UUID id) {
        String sql = "DELETE FROM campo WHERE idCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar campo: " + e.getMessage(), e);
        }
    }

    @Override
    public void removerPorIdPerfil(UUID idPerfil) {
        String sql = "DELETE FROM perfil_has_campo WHERE perfil_idPerfil = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idPerfil.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar campo por ID de perfil: " + e.getMessage(), e);
        }
    }

    @Override
    public Campo buscarPorId(UUID id) {
        String sql = "SELECT * FROM campo WHERE idCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Campo(UUID.fromString(rs.getString("idCampo")), rs.getString("tipoCampo"), rs.getString("nomeCampo"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar campo por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Campo buscarPorNome(String nome) {
        String sql = "SELECT * FROM campo WHERE nomeCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Campo(UUID.fromString(rs.getString("idCampo")), rs.getString("tipoCampo"), rs.getString("nomeCampo"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar campo por nome: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Campo> listarTodos() {
        List<Campo> campos = new ArrayList<>();
        String sql = "SELECT * FROM campo";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                campos.add(new Campo(UUID.fromString(rs.getString("idCampo")), rs.getString("tipoCampo"), rs.getString("nomeCampo")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar campos: " + e.getMessage(), e);
        }
        return campos;
    }

    @Override
    public List<Campo> listarTodosPorTipo(String tipo) {
        List<Campo> campos = new ArrayList<>();
        String sql = "SELECT * FROM campo WHERE tipoCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                campos.add(new Campo(
                        UUID.fromString(rs.getString("idCampo")),
                        rs.getString("tipoCampo"),
                        rs.getString("nomeCampo")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar campos: " + e.getMessage(), e);
        }
        return campos;
    }

    @Override
    public Integer buscarDiasPorProjetoCampo(UUID idProjeto, UUID idCampo) {
        int dias = 0;
        String sql = "SELECT diasProjeto FROM projeto_has_campo WHERE projeto_idProjeto = ? AND campo_idCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idProjeto.toString());
            stmt.setString(2, idCampo.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dias = rs.getInt("diasProjeto");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar dias por projeto e campo: " + e.getMessage(), e);
        }
        return dias;
    }

    @Override
    public Double buscarDiasPorPerfilCampo(UUID idPerfil, UUID idCampo) {
        Double dias = 0.0;
        String sql = "SELECT diasPerfil FROM perfil_has_campo WHERE perfil_idPerfil = ? AND campo_idCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idPerfil.toString());
            stmt.setString(2, idCampo.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dias = rs.getDouble("diasPerfil");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar dias por perfil e campo: " + e.getMessage(), e);
        }
        return dias;
    }

    @Override
    public boolean isCampoInPerfil(UUID idPerfil, UUID idCampo) {
        String sql = "SELECT * FROM perfil_has_campo WHERE perfil_idPerfil = ? AND campo_idCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idPerfil.toString());
            stmt.setString(2, idCampo.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar dias por perfil e campo: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Integer buscarDiasPorProjeto(UUID idProjeto) {
        Integer dias = null;
        String sql = "SELECT diasProjeto FROM projeto_has_campo WHERE projeto_idProjeto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idProjeto.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dias = rs.getInt("diasProjeto");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar dias por ID de projeto: " + e.getMessage(), e);
        }
        return dias;
    }

    @Override
    public Double buscarDiasPorPerfil(UUID idPerfil) {
        Double dias = null;
        String sql = "SELECT diasPerfil FROM perfil_has_campo WHERE perfil_idPerfil = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idPerfil.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dias = rs.getDouble("diasPerfil");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar dias por ID de perfil: " + e.getMessage(), e);
        }
        return dias;
    }

    @Override
    public List<Campo> listarTodosPorIdProjeto(UUID idProjeto) {
        List<Campo> campos = new ArrayList<>();
        String sql = "SELECT * FROM projeto_has_campo WHERE projeto_idProjeto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idProjeto.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Campo campo = buscarPorId(UUID.fromString(rs.getString("campo_idCampo")));
                if (campo != null) {
                    campo.setDias(buscarDiasPorProjetoCampo(idProjeto, campo.getId()).doubleValue());
                    campos.add(campo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar campos por projeto e campo: " + e.getMessage(), e);
        }
        return campos;
    }

    @Override
    public List<Campo> listarTodosPorIdPerfil(UUID idPerfil) {
        List<Campo> campos = new ArrayList<>();
        String sql = "SELECT * FROM perfil_has_campo WHERE perfil_idPerfil = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idPerfil.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Campo campo = buscarPorId(UUID.fromString(rs.getString("campo_idCampo")));
                if (campo != null) {
                    campo.setDias(buscarDiasPorPerfilCampo(idPerfil, campo.getId()));
                    campos.add(campo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar campos por projeto e campo: " + e.getMessage(), e);
        }
        return campos;
    }

    @Override
    public Campo buscarPorIdProjeto(UUID idProjeto) {
        String sql = "SELECT * FROM campo WHERE idCampo IN (SELECT campo_idCampo FROM projeto_has_campo WHERE projeto_idProjeto = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idProjeto.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer dias = buscarDiasPorProjeto(idProjeto);
                return new Campo(UUID.fromString(rs.getString("idCampo")), rs.getString("tipoCampo"), rs.getString("nomeCampo"), dias.doubleValue());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar campo por ID de projeto: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Campo buscarPorIdPerfil(UUID idPerfil) {
        String sql = "SELECT * FROM campo WHERE idCampo IN (SELECT campo_idCampo FROM perfil_has_campo WHERE perfil_idPerfil = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idPerfil.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double dias = buscarDiasPorPerfil(idPerfil);
                return new Campo(UUID.fromString(rs.getString("idCampo")), rs.getString("tipoCampo"), rs.getString("nomeCampo"), dias);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar campo por ID de perfil: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Campo> buscarPorIdProjetoTipo(UUID idProjeto, String tipo) {
        List<Campo> campos = new ArrayList<>();
        String sql = "SELECT * FROM campo WHERE idCampo IN (SELECT campo_idCampo FROM projeto_has_campo WHERE projeto_idProjeto = ?) AND tipoCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idProjeto.toString());
            stmt.setString(2, tipo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Campo campo = buscarPorId(UUID.fromString(rs.getString("idCampo")));
                if (campo != null) {
                    campo.setDias(buscarDiasPorProjetoCampo(idProjeto, campo.getId()).doubleValue());
                    campos.add(campo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar campo por ID de projeto e tipo: " + e.getMessage(), e);
        }
        return campos;
    }

    @Override
    public List<Campo> buscarPorIdPerfilTipo(UUID idPerfil, String tipo) {
        List<Campo> campos = new ArrayList<>();
        String sql = "SELECT * FROM campo WHERE idCampo IN (SELECT campo_idCampo FROM perfil_has_campo WHERE perfil_idPerfil = ?) AND tipoCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idPerfil.toString());
            stmt.setString(2, tipo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Campo campo = buscarPorId(UUID.fromString(rs.getString("idCampo")));
                if (campo != null) {
                    campo.setDias(buscarDiasPorPerfilCampo(idPerfil, campo.getId()));
                    campos.add(campo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar campo por ID de perfil e tipo: " + e.getMessage(), e);
        }
        return campos;
    }

    @Override
    public double buscarValorPorNomeProjetoCampo(UUID idProjeto, String nome) {
        String sql = "SELECT * FROM campo WHERE idCampo IN (SELECT campo_idCampo FROM projeto_has_campo WHERE projeto_idProjeto = ?) AND nomeCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idProjeto.toString());
            stmt.setString(2, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Campo campo = buscarPorId(UUID.fromString(rs.getString("idCampo")));
                if (campo != null) {
                    return (buscarDiasPorProjetoCampo(idProjeto, campo.getId()));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar campo por ID de projeto e nome de campo: " + e.getMessage(), e);
        }
        return 0.0;
    }
}
