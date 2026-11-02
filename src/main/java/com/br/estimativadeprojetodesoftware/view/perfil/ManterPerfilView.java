package com.br.estimativadeprojetodesoftware.view.perfil;

import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.view.componentes.Formulario;
import com.br.estimativadeprojetodesoftware.view.componentes.JanelaInterna;
import com.br.estimativadeprojetodesoftware.view.componentes.TabelaPadrao;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/** Edição de perfil: funcionalidades, valores por plataforma e taxas. Passive View. */
public class ManterPerfilView extends JanelaInterna {

    private final JTextField txtNome = Formulario.placeholder(new JTextField(30), "Nome do perfil");
    private final TabelaPadrao tabelaFuncionalidades = new TabelaPadrao();
    private final TabelaPadrao tabelaTaxas = new TabelaPadrao();
    private final Formulario formulario = new Formulario();
    private final JButton btnEditar;
    private final JButton btnSalvar;
    private final JButton btnCancelar;
    private final JButton btnNovaFuncionalidade;
    private final JButton btnRemoverFuncionalidade;
    private final JButton btnExcluir;
    private final JButton btnFechar;

    public ManterPerfilView() {
        super("Perfil", " ", "perfil");
        btnEditar = getBarra().adicionar("Editar", "editar");
        btnSalvar = getBarra().adicionarPrincipal("Salvar", "salvar");
        btnCancelar = getBarra().adicionar("Cancelar", "cancelar");
        getBarra().separador();
        btnNovaFuncionalidade = getBarra().adicionar("Nova funcionalidade", "adicionar");
        btnRemoverFuncionalidade = getBarra().adicionar("Remover funcionalidade", "remover");
        getBarra().separador();
        btnExcluir = getBarra().adicionarPerigo("Excluir", "excluir");
        getBarra().empurrarParaDireita();
        btnFechar = getBarra().adicionar("Fechar", "fechar");
        setBotaoSalvar(btnSalvar);

        formulario.adicionarCampo("Nome", txtNome);
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Funcionalidades", IconService.getIcon("projeto", 16), new JScrollPane(tabelaFuncionalidades),
                "Dias ou percentual por plataforma; NA = não aplicável");
        abas.addTab("Taxas diárias", IconService.getIcon("calcular", 16), new JScrollPane(tabelaTaxas), "Valor do dia por papel e plataforma");
        tabelaTaxas.setFormatoColuna(1, TabelaPadrao.Formato.REAIS);
        getConteudo().add(formulario, BorderLayout.NORTH);
        getConteudo().add(abas, BorderLayout.CENTER);
        setSize(1060, 660);
    }

    public void mostrarErroCampo(JComponent campo, String mensagem) {
        formulario.mostrarErro(campo, mensagem);
    }

    public void limparErroCampo(JComponent campo) {
        formulario.limparErro(campo);
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public JTextField getTxtNome() {
        return txtNome;
    }

    /** Linha de situação exibida sob o título. */
    public JLabel getLblSituacao() {
        return getLblSubtitulo();
    }

    public JTable getTabelaFuncionalidades() {
        return tabelaFuncionalidades;
    }

    public JTable getTabelaTaxas() {
        return tabelaTaxas;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JButton getBtnExcluir() {
        return btnExcluir;
    }

    public JButton getBtnNovaFuncionalidade() {
        return btnNovaFuncionalidade;
    }

    public JButton getBtnRemoverFuncionalidade() {
        return btnRemoverFuncionalidade;
    }

    public JButton getBtnFechar() {
        return btnFechar;
    }
}
