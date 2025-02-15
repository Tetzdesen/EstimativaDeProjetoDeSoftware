package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.*;
import java.util.Map;

public class CriarBarraService {

    private final Map<String, ProjetoCommand> comandos;
    
    public CriarBarraService(Map<String, ProjetoCommand> comandos) {
        this.comandos = comandos;
    }

    public JToolBar criarBarra() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        adicionarBotao(toolBar, "Dashboard", "principal", "Principal");
        adicionarBotao(toolBar, "Novo Projeto", "projeto", "Novo projeto");
        adicionarBotao(toolBar, "Usuário", "usuario", "Usuário");

        // Adicionando espaço flexível para empurrar o usuário logado para a direita
        toolBar.add(Box.createHorizontalGlue());
        
        adicionarLabel(toolBar, UsuarioLogadoSingleton.getInstancia().getUsuario().getNome());
        
        toolBar.add(Box.createHorizontalStrut(25));
                 
        adicionarBotao(toolBar, "Logout", "logout", "Logout");

        return toolBar;
    }
    
    public JToolBar criarBarraVisualizacaoUsuario() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        adicionarBotao(toolBar, "Salvar", "salvar", "Salvar usuário");
        adicionarBotao(toolBar, "Editar", "editar", "Editar usuário");
        adicionarBotao(toolBar, "Excluir", "excluir", "Excluir usuário");

        return toolBar;
    }

    private void adicionarBotao(JToolBar toolBar, String texto, String iconeKey, String comandoChave) {
        JButton botao = new JButton(texto);
        botao.setIcon(IconService.getIcon(iconeKey));
        botao.addActionListener(e -> {
            ProjetoCommand comando = comandos.get(comandoChave);
            if (comando == null) {
                throw new IllegalArgumentException("Comando não encontrado: " + comandoChave);
            }
            comando.execute();
        });
        toolBar.add(botao);
    }

    private void adicionarLabel(JToolBar toolBar, String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        toolBar.add(label);
    }

}
