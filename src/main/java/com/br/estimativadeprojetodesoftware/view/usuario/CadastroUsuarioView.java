package com.br.estimativadeprojetodesoftware.view.usuario;

import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.view.componentes.Estilos;
import com.br.estimativadeprojetodesoftware.view.componentes.Formulario;
import com.br.estimativadeprojetodesoftware.view.componentes.PainelCartao;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/** Criação de conta. Passive View; Enter confirma. */
public class CadastroUsuarioView extends JFrame {

    private final JTextField txtNomeUsuario = Formulario.placeholder(new JTextField(22), "Seu nome completo");
    private final JTextField txtEmail = Formulario.placeholder(new JTextField(22), "nome@exemplo.com");
    private final JPasswordField txtSenha = Estilos.aplicar(new JPasswordField(22), "showRevealButton: true");
    private final JPasswordField txtConfirmarSenha = Estilos.aplicar(new JPasswordField(22), "showRevealButton: true");
    private final JButton btnSalvar = Estilos.aplicar(new JButton("Criar conta", IconService.getIconContraste("usuario", 18)),
            Estilos.BOTAO_PRINCIPAL);
    private final JButton btnCancelar = new JButton("Cancelar");
    private final Formulario formulario = new Formulario();

    public CadastroUsuarioView() {
        super("Criar conta");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        PainelCartao painel = new PainelCartao("Criar conta",
                "A senha precisa atender às regras do validador<br>(até 10 caracteres, com letras, números e símbolos).", 440);
        formulario.adicionarCampo("Nome", txtNomeUsuario);
        formulario.adicionarCampo("E-mail", txtEmail);
        formulario.adicionarCampo("Senha", txtSenha);
        formulario.adicionarCampo("Confirmar senha", txtConfirmarSenha);
        painel.adicionar(formulario);
        JPanel botoes = new JPanel(new GridLayout(1, 2, 10, 0));
        botoes.setOpaque(false);
        botoes.add(PainelCartao.botaoLargo(btnCancelar));
        botoes.add(PainelCartao.botaoLargo(btnSalvar));
        painel.adicionar(botoes);
        setContentPane(painel);
        setIconImage(IconService.getIcon("logo", 32).getImage());
        setSize(600, 620);
        setResizable(false);
        getRootPane().setDefaultButton(btnSalvar);
    }

    public void mostrarErroCampo(JComponent campo, String mensagem) {
        formulario.mostrarErro(campo, mensagem);
        javax.swing.SwingUtilities.invokeLater(this::ajustarAltura);
    }

    /** Cresce a janela quando as mensagens de erro não cabem. */
    private void ajustarAltura() {
        int necessaria = getContentPane().getPreferredSize().height + getInsets().top + getInsets().bottom;
        if (necessaria > getHeight()) {
            setSize(getWidth(), necessaria);
        }
    }

    public void limparErroCampo(JComponent campo) {
        formulario.limparErro(campo);
    }

    public void limparErros() {
        formulario.limparErros();
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public JTextField getTxtNomeUsuario() {
        return txtNomeUsuario;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JPasswordField getTxtSenha() {
        return txtSenha;
    }

    public JPasswordField getTxtConfirmarSenha() {
        return txtConfirmarSenha;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }
}
