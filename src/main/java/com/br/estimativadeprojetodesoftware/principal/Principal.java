package com.br.estimativadeprojetodesoftware.principal;

import java.util.List;
import java.util.ArrayList;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Dispositivo;
import com.br.estimativadeprojetodesoftware.model.Perfil;

/**
 *
 * @author tetzner
 */
public class Principal {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        List<Dispositivo> dispositos = new ArrayList<>();
        Dispositivo ios = new IOS();
        ios.addCampo(new Campo("Tamanho do app", "pequeno", 10));
        ios.addCampo(new Campo("Tamanho do app", "Médio", 30));
        ios.addCampo(new Campo("Tamanho do app", "grande", 50));


        Dispositivo android = new Android();
        android.addCampo(new Campo("Tamanho do app", "pequeno", 10));
        android.addCampo(new Campo("Tamanho do app", "Médio", 30));
        android.addCampo(new Campo("Tamanho do app", "grande", 50));

        dispositos.add(ios);
        dispositos.add(android);
        Perfil perfil1 = new Perfil("principal", dispositos);

        for (Campo campo : perfil1.getDispositivos().get(0).getCampos()) {
            System.out.println("Funcionalidade de Tamanho do app para dispositivos IOS: \n");
            if (campo.getCategoria().equals("Tamanho do app")) {
                System.out.println("[" + campo.getFuncionalidades().get("Tamanho do app") + "]");
            }
        }

        System.out.println("Lista de todos os dispositivos e suas respectivas funcionalidades:");
        for (Dispositivo dispositivo : perfil1.getDispositivos()) {
            System.out.println(dispositivo.getNome());
            for (Campo campo : dispositivo.getCampos()) {
                System.out.println(campo.getFuncionalidades());
            }
        }
        //perfil1.getDispositivos().get(0).getCampos().get(0).getFuncionalidades(); //retorna o map 
    }
}
