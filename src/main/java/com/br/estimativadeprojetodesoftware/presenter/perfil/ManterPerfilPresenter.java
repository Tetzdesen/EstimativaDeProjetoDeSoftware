package com.br.estimativadeprojetodesoftware.presenter.perfil;

import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.TipoSelecao;
import com.br.estimativadeprojetodesoftware.model.TipoValor;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.presenter.tabela.FuncionalidadesPerfilTableModel;
import com.br.estimativadeprojetodesoftware.presenter.tabela.TaxasTableModel;
import com.br.estimativadeprojetodesoftware.service.PerfilService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.state.perfil.EdicaoPerfilState;
import com.br.estimativadeprojetodesoftware.state.perfil.InclusaoPerfilState;
import com.br.estimativadeprojetodesoftware.state.perfil.ManterPerfilPresenterState;
import com.br.estimativadeprojetodesoftware.state.perfil.VisualizacaoPerfilState;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;
import java.util.UUID;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/** Presenter do perfil com State (Visualização, Inclusão, Edição) e Commands. */
public class ManterPerfilPresenter implements Observer {

    private final ManterPerfilView view;
    private final PerfilService service;
    private final Usuario usuario;
    private Perfil perfil;
    private ManterPerfilPresenterState estado;
    private FuncionalidadesPerfilTableModel modeloFuncionalidades;
    private TaxasTableModel modeloTaxas;

    private ManterPerfilPresenter() {
        this.view = new ManterPerfilView();
        this.service = new PerfilService();
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        view.getBtnEditar().addActionListener(e -> executar(() -> estado.editar()));
        view.getBtnSalvar().addActionListener(e -> executar(() -> estado.salvar()));
        view.getBtnCancelar().addActionListener(e -> executar(() -> estado.cancelar()));
        view.getBtnExcluir().addActionListener(e -> executar(() -> estado.excluir()));
        view.getBtnNovaFuncionalidade().addActionListener(e -> executar(() -> estado.adicionarFuncionalidade()));
        view.getBtnRemoverFuncionalidade().addActionListener(e -> executar(() -> estado.removerFuncionalidade()));
        view.getBtnFechar().addActionListener(e -> fechar());
        service.addObserver(this);
        com.br.estimativadeprojetodesoftware.presenter.helpers.ValidacaoVisual.limparAoEditar(view.getTxtNome(), view::limparErroCampo);
        view.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                service.removeObserver(ManterPerfilPresenter.this);
            }
        });
    }

    public static ManterPerfilPresenter paraPerfil(UUID id) {
        ManterPerfilPresenter p = new ManterPerfilPresenter();
        p.setPerfil(p.service.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado.")));
        p.setEstado(new VisualizacaoPerfilState(p));
        return p;
    }

    /** Novo perfil personalizado como cópia (Prototype) do perfil base. */
    public static ManterPerfilPresenter novo(Perfil base) {
        ManterPerfilPresenter p = new ManterPerfilPresenter();
        p.setPerfil(base.clonar("Cópia de " + base.getNome(), p.usuario.getId()));
        p.setEstado(new InclusaoPerfilState(p));
        return p;
    }

    private void executar(Runnable acao) {
        try {
            acao.run();
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    public void setEstado(ManterPerfilPresenterState estado) {
        this.estado = estado;
        estado.entrar();
    }

    public ManterPerfilPresenterState getEstado() {
        return estado;
    }

    private void setPerfil(Perfil perfil) {
        this.perfil = perfil;
        view.setTituloCabecalho(perfil.getNome());
        view.getTxtNome().setText(perfil.getNome());
        modeloFuncionalidades = new FuncionalidadesPerfilTableModel(perfil, this::erro);
        modeloTaxas = new TaxasTableModel(perfil::getTaxas, this::erro);
        view.getTabelaFuncionalidades().setModel(modeloFuncionalidades);
        view.getTabelaTaxas().setModel(modeloTaxas);
        view.getTabelaFuncionalidades().getColumnModel().getColumn(2)
                .setCellEditor(new DefaultCellEditor(new JComboBox<>(TipoSelecao.values())));
        view.getTabelaFuncionalidades().getColumnModel().getColumn(3)
                .setCellEditor(new DefaultCellEditor(new JComboBox<>(TipoValor.values())));
        view.getTabelaFuncionalidades().getColumnModel().getColumn(1).setPreferredWidth(280);
        view.getTabelaFuncionalidades().getColumnModel().getColumn(0).setPreferredWidth(220);
    }

    public void setEditavel(boolean editavel) {
        view.getTxtNome().setEditable(editavel);
        modeloFuncionalidades.setEditavel(editavel);
        modeloTaxas.setEditavel(editavel);
    }

    /** Copia o nome para o perfil; devolve false (com o campo destacado) se estiver vazio. */
    public boolean aplicarCampos() {
        if (view.getTabelaFuncionalidades().isEditing()) {
            view.getTabelaFuncionalidades().getCellEditor().stopCellEditing();
        }
        if (view.getTabelaTaxas().isEditing()) {
            view.getTabelaTaxas().getCellEditor().stopCellEditing();
        }
        if (view.getTxtNome().getText().isBlank()) {
            view.mostrarErroCampo(view.getTxtNome(), "Informe o nome do perfil.");
            return false;
        }
        perfil.setNome(view.getTxtNome().getText());
        view.setTituloCabecalho(perfil.getNome());
        return true;
    }

    public void atualizarTabelas() {
        modeloFuncionalidades.recarregar();
    }

    public Funcionalidade funcionalidadeSelecionada() {
        int linha = view.getTabelaFuncionalidades().getSelectedRow();
        return linha < 0 ? null : modeloFuncionalidades.getLinha(linha);
    }

    public void recarregar() {
        setPerfil(service.buscarPorId(perfil.getId()).orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado.")));
    }

    public boolean podeAlterar() {
        return service.podeAlterar(perfil, usuario);
    }

    public void erro(String texto) {
        ExibirMensagemCommand.erro(texto);
    }

    public void sucesso(String texto) {
        ExibirMensagemCommand.sucesso(texto);
    }

    public void fechar() {
        service.removeObserver(this);
        view.dispose();
    }

    @Override
    public void update(EventoDominio evento) {
        if (perfil == null || !evento.sobre(EventoDominio.Entidade.PERFIL) || !perfil.getId().equals(evento.id())) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            if (!view.isDisplayable()) {
                return;
            }
            if (evento.tipo() == EventoDominio.Tipo.EXCLUSAO) {
                fechar();
            } else if (!(estado instanceof EdicaoPerfilState)) {
                recarregar();
                setEstado(new VisualizacaoPerfilState(this));
            }
        });
    }

    public ManterPerfilView getView() {
        return view;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public PerfilService getService() {
        return service;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
