package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.CadastroProjetoView;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Hiago Lopes
 */
public class CadastroProjetoPresenter {

    private CadastroProjetoView view;
    private ProjetoRepositoryMock repositoryProjeto;
    private Usuario usuario;

    public CadastroProjetoPresenter(CadastroProjetoView view, ProjetoRepositoryMock repositoryProjeto) {
        this.view = view;
        this.repositoryProjeto = repositoryProjeto;
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        configuraView();
    }

    private void configuraView() {
        // view.setResizable(false);
        view.setClosable(true);
        view.setIconifiable(true);
        view.setResizable(false);
        view.setMaximizable(false);
        //   view.setLocationRelativeTo(null);
        configuraActionsListerns();
        carregarListaPerfis();
        //   view.setVisible(true);
    }

    private void configuraActionsListerns() {
        view.getBtnCadastrarProjeto().addActionListener(e -> {
            try {
                cadastrarProjeto();
            } catch (Exception ex) {
                exibirMensagem(ex.getMessage());
            }
        });

        view.getBtnCancelar().addActionListener(e -> {
            try {
                view.dispose();
            } catch (Exception ex) {
                exibirMensagem(ex.getMessage());
            }
        });

        view.getBtnAdicionarPerfil().addActionListener(e -> {
            try {
                adicionarPerfil();
            } catch (Exception ex) {
                exibirMensagem(ex.getMessage());
            }
        });

    }

    private void carregarListaPerfis() {

        List<Perfil> perfis = usuario.getPerfis();

        DefaultComboBoxModel<String> cmbPerfis = new DefaultComboBoxModel<>();

        for (Perfil perfil : perfis) {
            cmbPerfis.addElement(perfil.getNome());
        }

        view.getCbmPerfis().setModel(cmbPerfis);
    }

    private void cadastrarProjeto() throws Exception {

        String nomeProjeto = view.getTxtNome().getText();

        String tipoProjeto = view.getTxtTipoProjeto().getText();

    }

    private void adicionarPerfil() {

        String perfil = (String) view.getCbmPerfis().getSelectedItem();

        if (perfil == null || perfil.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Selecione um perfil válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!(view.getjList1().getModel() instanceof DefaultListModel)) {
            view.getjList1().setModel(new DefaultListModel<>()); 
        }

        DefaultListModel<String> model = (DefaultListModel<String>) view.getjList1().getModel();

        if (!model.contains(perfil)) {
            model.addElement(perfil);
        } else {
            JOptionPane.showMessageDialog(view, "Este perfil já foi adicionado!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public CadastroProjetoView getView() {
        return view;
    }

    private boolean camposInvalidos(String email, String nome, String senha, String senhaConfirmada) {
        return email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty() || nome == null || nome.trim().isEmpty() || senhaConfirmada == null || senhaConfirmada.isEmpty();
    }

    public void exibirMensagem(String mensagem) {
        new MostrarMensagemProjetoCommand(mensagem).execute();
    }
}
