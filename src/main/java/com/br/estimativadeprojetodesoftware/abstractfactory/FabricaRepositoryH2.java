package com.br.estimativadeprojetodesoftware.abstractfactory;

import com.br.estimativadeprojetodesoftware.repository.ICompartilhamentoRepository;
import com.br.estimativadeprojetodesoftware.repository.IConfiguracaoRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcCompartilhamentoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcConfiguracaoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.sql.Connection;

/**
 * Fábrica concreta para o H2. Os repositórios JDBC usam só SQL comum aos dois
 * bancos; as diferenças de dialeto ficam no DatabaseInitializer do banco.
 */
public class FabricaRepositoryH2 implements FabricaRepository {

    private Connection conexao() {
        return ConexaoSingleton.getInstancia().getConexao();
    }

    @Override
    public IUsuarioRepository criarUsuarioRepository() {
        return new JdbcUsuarioRepository(conexao());
    }

    @Override
    public IProjetoRepository criarProjetoRepository() {
        return new JdbcProjetoRepository(conexao());
    }

    @Override
    public IPerfilRepository criarPerfilRepository() {
        return new JdbcPerfilRepository(conexao());
    }

    @Override
    public ICompartilhamentoRepository criarCompartilhamentoRepository() {
        return new JdbcCompartilhamentoRepository(conexao());
    }

    @Override
    public IConfiguracaoRepository criarConfiguracaoRepository() {
        return new JdbcConfiguracaoRepository(conexao());
    }
}
