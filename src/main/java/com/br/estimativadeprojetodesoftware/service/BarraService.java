package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.componentes.Estilos;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

/** Monta a barra de ferramentas da janela principal, em grupos, com ícones SVG. */
public class BarraService {

    private final Map<String, Command> comandos;
    private final Map<String, JButton> botoes;
    private JLabel usuarioLabel;

    public BarraService(Map<String, Command> comandos) {
        this.comandos = comandos;
        this.botoes = new HashMap<>();
    }

    public JToolBar criarBarraPrincipal() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        adicionarBotao(toolBar, "Dashboard", "dashboard", "Dashboard", "Visão geral dos seus projetos");
        adicionarBotao(toolBar, "Projetos", "projeto", "Projetos", "Lista de projetos próprios e compartilhados");
        JButton novo = adicionarBotao(toolBar, "Novo projeto", "novo-projeto", "Novo projeto", "Criar projeto a partir de perfis (Ctrl+N)");
        Estilos.aplicar(novo, Estilos.BOTAO_PRINCIPAL);
        novo.setIcon(IconService.getIconContraste("novo-projeto", IconService.TAMANHO_PADRAO));
        toolBar.addSeparator();
        adicionarBotao(toolBar, "Perfis", "perfil", "Perfis", "Perfis de estimativa");
        toolBar.addSeparator();
        adicionarBotao(toolBar, "Configuração", "configuracao", "Configuração", "Tema e formato do log");

        toolBar.add(Box.createHorizontalGlue());

        usuarioLabel = Estilos.aplicar(new JLabel(nomeUsuario(), IconService.getIcon("usuario"), JLabel.LEADING), "font: bold");
        usuarioLabel.setToolTipText("Usuário logado");
        toolBar.add(usuarioLabel);
        toolBar.add(Box.createHorizontalStrut(8));
        adicionarBotao(toolBar, "Minha conta", "editar", "Usuário", "Dados da sua conta");
        adicionarBotao(toolBar, "Sair", "sair", "Logout", "Sair do sistema");
        return toolBar;
    }

    private JButton adicionarBotao(JToolBar toolBar, String texto, String iconeKey, String comandoChave, String dica) {
        JButton botao = new JButton(texto, IconService.getIcon(iconeKey));
        botao.setToolTipText(dica);
        botao.addActionListener(e -> executarComando(comandoChave));
        botoes.put(comandoChave, botao);
        if (toolBar.getComponentCount() > 0 && !(toolBar.getComponent(toolBar.getComponentCount() - 1) instanceof JToolBar.Separator)) {
            toolBar.add(Box.createHorizontalStrut(4));
        }
        toolBar.add(botao);
        return botao;
    }

    private static String nomeUsuario() {
        var u = UsuarioLogadoSingleton.getInstancia().getUsuario();
        return u == null ? "" : u.getNome();
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
            usuarioLabel.setText(nomeUsuario());
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
