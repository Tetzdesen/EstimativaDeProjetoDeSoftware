package com.br.estimativadeprojetodesoftware.view.usuario;

import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.view.componentes.BarraAcoes;
import com.br.estimativadeprojetodesoftware.view.componentes.Estilos;
import com.br.estimativadeprojetodesoftware.view.componentes.Formulario;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/** Dados da conta do usuário logado. Passive View; Esc fecha e Ctrl+S salva (se habilitado). */
public class ManterUsuarioView extends JDialog {

    private final JTextField txtNome = new JTextField(26);
    private final JTextField txtEmail = new JTextField(26);
    private final JPasswordField txtSenhaAtual = Estilos.aplicar(new JPasswordField(26), "showRevealButton: true");
    private final JTextField txtDataCriacao = somenteLeitura();
    private final JTextField txtTotalProjetos = somenteLeitura();
    private final JTextField txtTotalPerfis = somenteLeitura();
    private final BarraAcoes barra = new BarraAcoes();
    private final JButton btnEditar = barra.adicionar("Editar", "editar");
    private final JButton btnSalvar = barra.adicionarPrincipal("Salvar", "salvar");
    private final JButton btnExcluir;
    private final Formulario formulario = new Formulario();

    public ManterUsuarioView() {
        setTitle("Minha conta");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        barra.empurrarParaDireita();
        btnExcluir = barra.adicionarPerigo("Excluir conta", "excluir");
        Formulario.placeholder(txtSenhaAtual, "Deixe em branco para manter a atual");

        JLabel titulo = Estilos.aplicar(new JLabel("Minha conta", IconService.getIcon("usuario", 26), JLabel.LEADING), Estilos.TITULO);
        titulo.setIconTextGap(10);
        JLabel subtitulo = Estilos.aplicar(new JLabel("Dados de acesso e resumo do seu uso do sistema."), Estilos.SUBTITULO);

        formulario.adicionarSecao("Dados de acesso");
        formulario.adicionarCampo("Nome", txtNome);
        formulario.adicionarCampo("E-mail", txtEmail);
        formulario.adicionarCampo("Nova senha", txtSenhaAtual);
        formulario.adicionarSecao("Resumo");
        formulario.adicionarCampo("Conta criada em", txtDataCriacao);
        formulario.adicionarCampo("Meus projetos", txtTotalProjetos);
        formulario.adicionarCampo("Meus perfis", txtTotalPerfis);

        JPanel topo = new JPanel(new BorderLayout(0, 4));
        topo.add(titulo, BorderLayout.NORTH);
        topo.add(subtitulo, BorderLayout.CENTER);
        topo.add(barra, BorderLayout.SOUTH);
        barra.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel raiz = new JPanel(new BorderLayout(0, 16));
        raiz.setBorder(BorderFactory.createEmptyBorder(18, 20, 20, 20));
        raiz.add(topo, BorderLayout.NORTH);
        JPanel corpo = new JPanel(new BorderLayout());
        corpo.add(formulario, BorderLayout.NORTH);
        raiz.add(corpo, BorderLayout.CENTER);
        setContentPane(raiz);
        registrarAtalhos();
        setSize(560, 520);
        setResizable(false);
    }

    private static JTextField somenteLeitura() {
        JTextField t = new JTextField(26);
        t.setEditable(false);
        t.setFocusable(false);
        return t;
    }

    private void registrarAtalhos() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "fechar");
        getRootPane().getActionMap().put("fechar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "salvar");
        getRootPane().getActionMap().put("salvar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnSalvar.isEnabled()) {
                    btnSalvar.doClick();
                }
            }
        });
    }

    public void mostrarErroCampo(JComponent campo, String mensagem) {
        formulario.mostrarErro(campo, mensagem);
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

    public JTextField getTxtNome() {
        return txtNome;
    }

    public JTextField getTxtDataCriacao() {
        return txtDataCriacao;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JPasswordField getTxtSenhaAtual() {
        return txtSenhaAtual;
    }

    public JTextField getTxtTotalProjetos() {
        return txtTotalProjetos;
    }

    public JTextField getTxtTotalPerfis() {
        return txtTotalPerfis;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnExcluir() {
        return btnExcluir;
    }
}
