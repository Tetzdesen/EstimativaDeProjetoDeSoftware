package com.br.estimativadeprojetodesoftware.abstractfactory;

import com.br.estimativadeprojetodesoftware.service.DotenvService;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 *
 * @author tetzner
 */
public class SeletorFabricaRepository {
    
    private SeletorFabricaRepository(){
        
    }

    private static final Map<String, String> classMap = Map.of(
            "SQLite", "com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepositorySQLite",
            "H2", "com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepositoryH2"
    );

    public static FabricaRepository obterInstancia() {

        String banco = DotenvService.getEnv("TIPO_BANCO");

        String tipoBanco = classMap.get(banco);
        
        System.out.println(tipoBanco);
        
        try {
            Class<?> nomeClasse = Class.forName(tipoBanco);
            var construtor = nomeClasse.getConstructor();
            return (FabricaRepository) construtor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("Erro na reflexão de fábrica da repository, tipo de banco: " + tipoBanco + "Erro: " + ex.getMessage());
        }
    }
}
