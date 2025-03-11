package com.br.estimativadeprojetodesoftware.repository.sqlite;

import com.br.estimativadeprojetodesoftware.repository.IProjetoHasPerfilRepository;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjetoHasPerfilRepositorySQLite implements IProjetoHasPerfilRepository {

    private final Connection conexao;

    public ProjetoHasPerfilRepositorySQLite() {
        this.conexao = ConexaoSingleton.getInstancia().getConexao();
    }

    @Override
    public void salvar(String projetoId, String perfilId) {
        String sql = "INSERT INTO projeto_has_perfil (projeto_idProjeto, perfil_idPerfil) VALUES (?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, projetoId);
            stmt.setString(2, perfilId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao adicionar relação projeto-perfil: " + e.getMessage(), e);
        }
    }

    @Override
    public void removerPorIds(String projetoId, String perfilId) {
        String sql = "DELETE FROM projeto_has_perfil WHERE projeto_idProjeto = ? AND perfil_idPerfil = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, projetoId);
            stmt.setString(2, perfilId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover relação projeto-perfil: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> buscarPerfisPorProjeto(String projetoId) {
        List<String> perfis = new ArrayList<>();
        String sql = "SELECT perfil_idPerfil FROM projeto_has_perfil WHERE projeto_idProjeto = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, projetoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                perfis.add(rs.getString("perfil_idPerfil"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar perfis por projeto: " + e.getMessage(), e);
        }
        return perfis;
    }

    @Override
    public List<String> buscarProjetosPorPerfil(String perfilId) {
        List<String> projetos = new ArrayList<>();
        String sql = "SELECT projeto_idProjeto FROM projeto_has_perfil WHERE perfil_idPerfil = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, perfilId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                projetos.add(rs.getString("projeto_idProjeto"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar projetos por perfil: " + e.getMessage(), e);
        }
        return projetos;
    }
}
