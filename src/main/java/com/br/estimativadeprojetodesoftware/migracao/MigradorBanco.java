package com.br.estimativadeprojetodesoftware.migracao;

import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import com.br.estimativadeprojetodesoftware.strategy.DatabaseInitializer;
import com.br.estimativadeprojetodesoftware.strategy.EsquemaBanco;
import io.github.cdimascio.dotenv.Dotenv;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copia todos os dados de um banco para outro (ex.: SQLite local para o
 * PostgreSQL do Supabase). Origem e destino vêm de arquivos .env, para a senha
 * não aparecer na linha de comando. O destino precisa estar sem usuários e
 * sem perfis, e a cópia acontece numa única transação.
 *
 * <pre>java -cp target/estimativa.jar com.br.estimativadeprojetodesoftware.migracao.MigradorBanco origem.env destino.env</pre>
 */
public final class MigradorBanco {

    private static final Pattern NOME_TABELA = Pattern.compile("CREATE TABLE IF NOT EXISTS (\\w+)");

    private MigradorBanco() {
    }

    /** Falha na migração, com a tabela onde ocorreu. */
    public static class MigracaoException extends RuntimeException {

        public MigracaoException(String mensagem, Throwable causa) {
            super(mensagem, causa);
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Uso: MigradorBanco <origem.env> <destino.env>");
            System.exit(2);
        }
        try (Connection origem = conectar(Path.of(args[0]), false);
                Connection destino = conectar(Path.of(args[1]), true)) {
            Map<String, long[]> contagens = migrar(origem, destino);
            System.out.printf("%-26s %8s %8s%n", "tabela", "origem", "destino");
            boolean confere = true;
            for (var e : contagens.entrySet()) {
                System.out.printf("%-26s %8d %8d%n", e.getKey(), e.getValue()[0], e.getValue()[1]);
                confere &= e.getValue()[0] == e.getValue()[1];
            }
            System.out.println(confere ? "Migração concluída." : "ATENÇÃO: as contagens não conferem.");
            System.exit(confere ? 0 : 1);
        } catch (RuntimeException | SQLException e) {
            System.err.println("Migração não realizada: " + e.getMessage());
            System.exit(1);
        }
    }

    /** Abre a conexão descrita no .env; no destino, cria as tabelas que faltam (sem seeders). */
    private static Connection conectar(Path arquivo, boolean criarTabelas) throws SQLException {
        if (!Files.isRegularFile(arquivo)) {
            throw new IllegalArgumentException("Arquivo não encontrado: " + arquivo);
        }
        Path pasta = arquivo.toAbsolutePath().getParent();
        Dotenv env = Dotenv.configure().directory(pasta.toString()).filename(arquivo.getFileName().toString()).load();
        String tipo = valor(env, "TIPO_BANCO");
        String url = valor(env, "DB_URL");
        if (tipo == null || url == null) {
            throw new IllegalArgumentException("Defina TIPO_BANCO e DB_URL em " + arquivo);
        }
        DatabaseInitializer inicializador = ConexaoSingleton.inicializadorPara(tipo);
        Connection conexao = ConexaoSingleton.conectar(inicializador, url, valor(env, "DB_USER"), valor(env, "DB_PASSWORD"));
        if (criarTabelas) {
            inicializador.criarTabelas(conexao);
        }
        return conexao;
    }

    private static String valor(Dotenv env, String chave) {
        String v = env.get(chave, null);
        if (v == null) {
            return null;
        }
        v = v.trim();
        if (v.length() >= 2 && v.startsWith("\"") && v.endsWith("\"")) {
            v = v.substring(1, v.length() - 1).trim();
        }
        return v.isEmpty() ? null : v;
    }

    /** Tabelas na ordem de criação, que respeita as chaves estrangeiras. */
    public static List<String> tabelas() {
        List<String> nomes = new ArrayList<>();
        for (String ddl : EsquemaBanco.TABELAS) {
            Matcher m = NOME_TABELA.matcher(ddl);
            if (m.find()) {
                nomes.add(m.group(1));
            }
        }
        return nomes;
    }

    /**
     * Copia os dados e devolve, por tabela, a contagem na origem e no destino.
     * O destino já precisa ter as tabelas.
     */
    public static Map<String, long[]> migrar(Connection origem, Connection destino) {
        if (contar(destino, "usuario") > 0 || contar(destino, "perfil") > 0) {
            throw new MigracaoException("O banco de destino já tem usuários ou perfis; a migração precisa de um destino vazio.", null);
        }
        boolean autoCommit;
        try {
            autoCommit = destino.getAutoCommit();
            destino.setAutoCommit(false);
        } catch (SQLException e) {
            throw new MigracaoException("Não foi possível iniciar a transação no destino: " + e.getMessage(), e);
        }
        String atual = null;
        try {
            for (String tabela : tabelas()) {
                atual = tabela;
                copiar(origem, destino, tabela);
            }
            destino.commit();
        } catch (SQLException | RuntimeException e) {
            try {
                destino.rollback();
            } catch (SQLException ignorada) {
                // a falha original é a que interessa
            }
            throw new MigracaoException("Falha ao copiar a tabela " + atual + ": " + e.getMessage(), e);
        } finally {
            try {
                destino.setAutoCommit(autoCommit);
            } catch (SQLException ignorada) {
                // conexão provavelmente perdida; a mensagem de erro já foi dada
            }
        }
        Map<String, long[]> contagens = new LinkedHashMap<>();
        for (String tabela : tabelas()) {
            contagens.put(tabela, new long[]{contar(origem, tabela), contar(destino, tabela)});
        }
        return contagens;
    }

    private static void copiar(Connection origem, Connection destino, String tabela) throws SQLException {
        List<String> colunas = new ArrayList<>();
        List<Integer> tipos = new ArrayList<>();
        try (Statement st = destino.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM " + tabela + " WHERE 1 = 0")) {
            ResultSetMetaData md = rs.getMetaData();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                colunas.add(md.getColumnName(i).toLowerCase());
                tipos.add(md.getColumnType(i));
            }
        }
        String lista = String.join(", ", colunas);
        String marcadores = String.join(", ", colunas.stream().map(c -> "?").toList());
        try (Statement st = origem.createStatement();
                ResultSet rs = st.executeQuery("SELECT " + lista + " FROM " + tabela);
                PreparedStatement ins = destino.prepareStatement("INSERT INTO " + tabela + " (" + lista + ") VALUES (" + marcadores + ")")) {
            int lote = 0;
            while (rs.next()) {
                for (int i = 0; i < colunas.size(); i++) {
                    Object valor = converter(rs.getObject(i + 1), tipos.get(i));
                    if (valor == null) {
                        ins.setNull(i + 1, tipos.get(i));
                    } else {
                        ins.setObject(i + 1, valor);
                    }
                }
                ins.addBatch();
                if (++lote % 500 == 0) {
                    ins.executeBatch();
                }
            }
            ins.executeBatch();
        }
    }

    /** SQLite guarda booleanos como 0/1; o destino pode exigir booleano de verdade. */
    private static Object converter(Object valor, int tipoDestino) {
        if (valor == null) {
            return null;
        }
        if (tipoDestino == Types.BOOLEAN || tipoDestino == Types.BIT) {
            if (valor instanceof Number n) {
                return n.intValue() != 0;
            }
            if (valor instanceof String s) {
                return s.equals("1") || s.equalsIgnoreCase("true");
            }
        }
        if ((tipoDestino == Types.INTEGER || tipoDestino == Types.SMALLINT) && valor instanceof Number n) {
            return n.intValue();
        }
        if ((tipoDestino == Types.VARCHAR || tipoDestino == Types.CHAR) && !(valor instanceof String)) {
            return valor.toString();
        }
        return valor;
    }

    private static long contar(Connection conexao, String tabela) {
        try (Statement st = conexao.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + tabela)) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new MigracaoException("Não foi possível contar " + tabela + ": " + e.getMessage(), e);
        }
    }
}
