package com.br.estimativadeprojetodesoftware.strategy;

/**
 *
 * @author tetzner
 */
public interface DatabaseInitializer {

    /**
     * Prepara o armazenamento do banco antes da conexão ser aberta.
     *
     * @return true quando o banco acabou de ser criado e ainda precisa ter a
     * estrutura configurada.
     */
    boolean prepararArmazenamento(String url, String user, String senha);

    /**
     * Cria as tabelas e executa os seeders. Só deve ser chamado depois que a
     * conexão já estiver disponível.
     */
    void configurarBanco();
}
