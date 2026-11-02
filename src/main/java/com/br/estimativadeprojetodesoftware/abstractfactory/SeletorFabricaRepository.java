package com.br.estimativadeprojetodesoftware.abstractfactory;

import com.br.estimativadeprojetodesoftware.service.DotenvService;

/**
 * Instancia, por reflexão, a fábrica cujo nome de classe está na chave do .env
 * de mesmo nome que TIPO_BANCO (ex.: TIPO_BANCO = "H2" e H2 = "...FabricaRepositoryH2").
 */
public class SeletorFabricaRepository {

    private static FabricaRepository instancia;

    private SeletorFabricaRepository() {
    }

    public static synchronized FabricaRepository obterInstancia() {
        if (instancia != null) {
            return instancia;
        }
        String tipoBanco = DotenvService.getValor("TIPO_BANCO");
        if (tipoBanco == null) {
            throw new IllegalStateException("Variável TIPO_BANCO não definida no .env");
        }
        String nomeClasse = DotenvService.getValor(tipoBanco);
        if (nomeClasse == null) {
            throw new IllegalStateException("Chave \"" + tipoBanco + "\" com a fábrica de repositórios não definida no .env");
        }
        try {
            Class<?> classe = Class.forName(nomeClasse);
            instancia = (FabricaRepository) classe.getConstructor().newInstance();
            return instancia;
        } catch (ReflectiveOperationException | ClassCastException e) {
            throw new IllegalStateException("Erro ao criar a fábrica de repositórios para o banco " + tipoBanco + ": " + e.getMessage(), e);
        }
    }
}
