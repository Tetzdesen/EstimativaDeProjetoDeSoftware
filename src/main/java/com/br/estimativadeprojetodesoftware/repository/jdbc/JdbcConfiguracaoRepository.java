package com.br.estimativadeprojetodesoftware.repository.jdbc;

import static com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcSuporte.executar;

import com.br.estimativadeprojetodesoftware.repository.IConfiguracaoRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

/** Repository chave/valor da tabela configuracao. */
public class JdbcConfiguracaoRepository implements IConfiguracaoRepository {

    private final Connection conexao;

    public JdbcConfiguracaoRepository(Connection conexao) {
        this.conexao = conexao;
    }

    @Override
    public Optional<String> buscar(String chave) {
        return executar("Erro ao ler a configuração", () -> {
            try (PreparedStatement st = conexao.prepareStatement("SELECT valor FROM configuracao WHERE chave = ?")) {
                st.setString(1, chave);
                try (ResultSet rs = st.executeQuery()) {
                    return rs.next() ? Optional.ofNullable(rs.getString(1)) : Optional.<String>empty();
                }
            }
        });
    }

    @Override
    public void salvar(String chave, String valor) {
        JdbcSuporte.emTransacao(conexao, "Erro ao salvar a configuração", () -> {
            try (PreparedStatement del = conexao.prepareStatement("DELETE FROM configuracao WHERE chave = ?");
                    PreparedStatement ins = conexao.prepareStatement("INSERT INTO configuracao (chave, valor) VALUES (?, ?)")) {
                del.setString(1, chave);
                del.executeUpdate();
                ins.setString(1, chave);
                ins.setString(2, valor);
                return ins.executeUpdate();
            }
        });
    }
}
