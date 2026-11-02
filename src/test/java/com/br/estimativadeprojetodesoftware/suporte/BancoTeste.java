package com.br.estimativadeprojetodesoftware.suporte;

import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.UUID;

/** Abre bancos de teste: SQLite em arquivo temporário e H2 em memória. */
public final class BancoTeste {

    private BancoTeste() {
    }

    public static Connection abrir(String tipo, Path pastaTemporaria) {
        String url = switch (tipo) {
            case "SQLite" -> "jdbc:sqlite:" + pastaTemporaria.resolve("teste-" + UUID.randomUUID() + ".db");
            case "H2" -> "jdbc:h2:mem:teste" + UUID.randomUUID().toString().replace("-", "") + ";DB_CLOSE_DELAY=-1";
            default -> throw new IllegalArgumentException(tipo);
        };
        return ConexaoSingleton.abrir(ConexaoSingleton.inicializadorPara(tipo), url, null, null);
    }

    public static Connection abrirUrl(String tipo, String url) {
        return ConexaoSingleton.abrir(ConexaoSingleton.inicializadorPara(tipo), url, null, null);
    }
}
