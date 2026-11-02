package com.br.estimativadeprojetodesoftware.view.usuario;

import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.view.componentes.Estilos;
import com.br.estimativadeprojetodesoftware.view.componentes.PainelCartao;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/** Tela de boas-vindas: entrar ou criar conta. Passive View. */
public class PrincipalUsuarioView extends JFrame {

    private final JButton btnAutenticarPorSenha = PainelCartao.botaoLargo(
            Estilos.aplicar(new JButton("Entrar com e-mail e senha", IconService.getIconContraste("usuario", 18)), Estilos.BOTAO_PRINCIPAL));
    private final JButton btnCadastrar = PainelCartao.link("Criar conta");

    public PrincipalUsuarioView() {
        super("Estimativa de Projetos de Software");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        PainelCartao painel = new PainelCartao("Estimativa de Projetos",
                "Calcule prazo e custo de aplicativos<br>Web, iOS e Android a partir de perfis.", 420);
        painel.adicionar(btnAutenticarPorSenha);
        JPanel cadastro = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        cadastro.setOpaque(false);
        cadastro.add(Estilos.aplicar(new JLabel("Ainda não tem conta?"), Estilos.SUBTITULO));
        cadastro.add(btnCadastrar);
        painel.adicionar(cadastro);
        setContentPane(painel);
        setIconImages(java.util.List.of(IconService.getIcon("logo", 32).getImage(), IconService.getIcon("logo", 16).getImage()));
        setSize(820, 560);
        getRootPane().setDefaultButton(btnAutenticarPorSenha);
    }

    public JButton getBtnAutenticarPorSenha() {
        return btnAutenticarPorSenha;
    }

    public JButton getBtnCadastrar() {
        return btnCadastrar;
    }
}
