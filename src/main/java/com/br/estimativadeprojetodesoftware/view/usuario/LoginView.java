package com.br.estimativadeprojetodesoftware.view.usuario;

import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.view.componentes.Estilos;
import com.br.estimativadeprojetodesoftware.view.componentes.Formulario;
import com.br.estimativadeprojetodesoftware.view.componentes.PainelCartao;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/** Login por e-mail e senha. Passive View; Enter confirma (botão padrão). */
public class LoginView extends JFrame implements com.br.estimativadeprojetodesoftware.view.componentes.Ocupavel {

    private final JTextField txtEmail = Formulario.placeholder(new JTextField(22), "nome@exemplo.com");
    private final JPasswordField txtSenha = new JPasswordField(22);
    private final JButton btnEntrar = PainelCartao.botaoLargo(
            Estilos.aplicar(new JButton("Entrar", IconService.getIconContraste("sair", 18)), Estilos.BOTAO_PRINCIPAL));
    private final JButton btnCriarConta = PainelCartao.link("Criar conta");
    private final JLabel lblErroGeral = Estilos.aplicar(new JLabel(" "), Estilos.ERRO_CAMPO);
    private final Formulario formulario = new Formulario();

    public LoginView() {
        super("Entrar");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Estilos.aplicar(txtSenha, "showRevealButton: true");
        PainelCartao painel = new PainelCartao("Entrar", "Use o e-mail e a senha da sua conta.", 380);
        formulario.adicionarCampo("E-mail", txtEmail);
        formulario.adicionarCampo("Senha", txtSenha);
        painel.adicionar(formulario);
        lblErroGeral.setVisible(false);
        painel.adicionar(lblErroGeral);
        painel.adicionar(btnEntrar);
        JPanel cadastro = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        cadastro.setOpaque(false);
        cadastro.add(Estilos.aplicar(new JLabel("Não tem conta?"), Estilos.SUBTITULO));
        cadastro.add(btnCriarConta);
        painel.adicionar(cadastro);
        setContentPane(painel);
        setIconImage(IconService.getIcon("logo", 32).getImage());
        setSize(560, 520);
        setResizable(false);
        getRootPane().setDefaultButton(btnEntrar);
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

    /** Erro que não pertence a um campo (ex.: credenciais inválidas). */
    public void mostrarErroGeral(String mensagem) {
        lblErroGeral.setText(mensagem == null ? " " : mensagem);
        lblErroGeral.setVisible(mensagem != null && !mensagem.isBlank());
    }

    public void limparErros() {
        formulario.limparErros();
        mostrarErroGeral(null);
    }

    private boolean ocupado;
    private String textoEntrar;

    /** Enquanto autentica: campos e botões desabilitados e o botão mostra o andamento. */
    @Override
    public void setOcupado(boolean ocupado, String mensagem) {
        if (ocupado && !this.ocupado) {
            textoEntrar = btnEntrar.getText();
        }
        this.ocupado = ocupado;
        btnEntrar.setText(ocupado && mensagem != null ? mensagem : textoEntrar);
        for (javax.swing.JComponent c : new javax.swing.JComponent[]{txtEmail, txtSenha, btnEntrar, btnCriarConta}) {
            c.setEnabled(!ocupado);
        }
        setCursor(ocupado ? java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR) : null);
    }

    @Override
    public boolean isOcupado() {
        return ocupado;
    }

    @Override
    public boolean isFechada() {
        return !isDisplayable();
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JPasswordField getTxtSenha() {
        return txtSenha;
    }

    public JButton getBtnEntrar() {
        return btnEntrar;
    }

    public JButton getBtnCriarConta() {
        return btnCriarConta;
    }

    public JLabel getLblErroGeral() {
        return lblErroGeral;
    }
}
