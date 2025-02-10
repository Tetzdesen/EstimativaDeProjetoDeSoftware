package com.br.estimativadeprojetodesoftware.principal;

import java.util.List;
import java.util.ArrayList;

import com.br.estimativadeprojetodesoftware.model.Perfil;

/**
 *
 * @author tetzner
 */
public class Principal {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Perfil ios = new Perfil("Perfil iOS");

        ios.adicionarTamDispositivo("Pequeno", 10);
        ios.adicionarTamDispositivo("Médio", 30);
        ios.adicionarTamDispositivo("Grande", 50);
        
        System.out.println(ios.getTamDispositivos());


       // Dispositivo android = new Dispositivo("Android");
      //  android.addCampo(new Campo("Tamanho do app", "pequeno", 10));
      //  android.addCampo(new Campo("Tamanho do app", "Médio", 30));
      //  android.addCampo(new Campo("Tamanho do app", "grande", 50));

   
      //  Perfil perfil1 = new Perfil("principal");
     //   perfil1.adicionarDispositivo(ios);
      //  perfil1.adicionarDispositivo(android);
        
    
        
    }
}
