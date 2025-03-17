package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.*;
import java.util.Map;
import com.br.estimativadeprojetodesoftware.command.Command;

public class BarraService {

    private final Map<String, Command> comandos;
    private final Map<String, JButton> botoes;
    private JLabel usuarioLabel;

    public BarraService(Map<String, Command> comandos) {
        this.comandos = comandos;
        this.botoes = new HashMap<>();
    }

    public JToolBar criarBarraPrincipal() {
        JToolBar toolBar = criarBarraBase();

        adicionarBotao(toolBar, "Dashboard", "principal", "Principal");
        adicionarBotao(toolBar, "Novo Projeto", "projeto", "Novo projeto");
        adicionarBotao(toolBar, "Usuário", "usuario", "Usuário");

        toolBar.add(Box.createHorizontalGlue());

        usuarioLabel = adicionarLabel(toolBar, UsuarioLogadoSingleton.getInstancia().getUsuario().getNome());

        toolBar.add(Box.createHorizontalStrut(25));

        adicionarBotao(toolBar, "Logout", "logout", "Logout");

        return toolBar;
    }

    public JToolBar criarBarraUsuario() {
        JToolBar toolBar = criarBarraBase();

        adicionarBotao(toolBar, "Salvar", "salvar", "Salvar usuário");
        adicionarBotao(toolBar, "Editar", "editar", "Editar usuário");
        adicionarBotao(toolBar, "Excluir", "excluir", "Excluir usuário");

        return toolBar;
    }

    private JToolBar criarBarraBase() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        return toolBar;
    }

    private JButton adicionarBotao(JToolBar toolBar, String texto, String iconeKey, String comandoChave) {
        JButton botao = new JButton(texto);
        botao.setIcon(IconService.getIcon(iconeKey));
        botao.addActionListener(e -> executarComando(comandoChave));

        botoes.put(comandoChave, botao);
        toolBar.add(botao);

        return botao;
    }

    private JLabel adicionarLabel(JToolBar toolBar, String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        toolBar.add(label);
        return label;
    }

    private void executarComando(String comandoChave) {
        Command comando = comandos.get(comandoChave);
        if (comando == null) {
            throw new IllegalArgumentException("Comando não encontrado: " + comandoChave);
        }
        comando.execute();
    }

    public void atualizarLabelUsuarioLogado() {
        if (usuarioLabel != null) {
            usuarioLabel.setText(UsuarioLogadoSingleton.getInstancia().getUsuario().getNome());
        }
    }

    public JButton getBotao(String chave) {
        return botoes.get(chave);
    }

    public JLabel getUsuarioLabel() {
        return usuarioLabel;
    }

    public Map<String, JButton> getTodosBotoes() {
        return botoes;
    }

}
