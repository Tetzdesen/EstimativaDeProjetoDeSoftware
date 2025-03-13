package com.br.estimativadeprojetodesoftware.abstractfactory;

import com.br.estimativadeprojetodesoftware.service.DotenvService;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author tetzner
 */
public class SeletorFabricaRepository {
    
    private SeletorFabricaRepository(){
        
    }

    public static FabricaRepository obterInstancia() {

        String tipoBanco = DotenvService.getEnv("TIPO_BANCO");
        
        try {
            Class<?> nomeClasse = Class.forName(DotenvService.getEnv(tipoBanco));
            var construtor = nomeClasse.getConstructor();
            return (FabricaRepository) construtor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("Erro na reflexão de fábrica da repository, tipo de banco: " + tipoBanco + "Erro: " + ex.getMessage());
        }
    }
}
