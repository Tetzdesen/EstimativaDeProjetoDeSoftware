package com.br.estimativadeprojetodesoftware.presenter.configuracao;

import com.br.estimativadeprojetodesoftware.command.AplicarTemaCommand;
import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.model.Tema;
import com.br.estimativadeprojetodesoftware.service.LogService;
import com.br.estimativadeprojetodesoftware.service.PreferenciasService;
import com.br.estimativadeprojetodesoftware.view.configuracao.ConfiguracaoView;
import com.github.tetzdesen.estimativalog.FormatoLog;

/**
 * Tela de Configuração: tema (claro/escuro) e formato do log. As escolhas são
 * gravadas na tabela configuracao e aplicadas na hora: o tema em todas as
 * janelas abertas e o log a partir da próxima operação.
 */
public class ConfiguracaoPresenter {

    private final ConfiguracaoView view;
    private final LogService logService;
    private final PreferenciasService preferencias;

    public ConfiguracaoPresenter() {
        this(new ConfiguracaoView(), LogService.getInstancia(), new PreferenciasService());
    }

    public ConfiguracaoPresenter(ConfiguracaoView view, LogService logService, PreferenciasService preferencias) {
        this.view = view;
        this.logService = logService;
        this.preferencias = preferencias;
        view.getBtnSalvar().addActionListener(e -> salvar());
        view.getBtnFechar().addActionListener(e -> view.dispose());
        exibir();
    }

    private void exibir() {
        Tema tema = preferencias.getTema();
        view.getRbClaro().setSelected(tema == Tema.CLARO);
        view.getRbEscuro().setSelected(tema == Tema.ESCURO);
        FormatoLog atual = logService.getFormato();
        view.getRbCsv().setSelected(atual == FormatoLog.CSV);
        view.getRbJson().setSelected(atual == FormatoLog.JSON);
        view.getLblArquivo().setText("Arquivo atual: " + logService.getArquivoAtual().toAbsolutePath());
    }

    public void salvar() {
        try {
            FormatoLog formato = view.getRbCsv().isSelected() ? FormatoLog.CSV : FormatoLog.JSON;
            Tema tema = view.getRbEscuro().isSelected() ? Tema.ESCURO : Tema.CLARO;
            logService.setFormato(formato);
            preferencias.setTema(tema);
            new AplicarTemaCommand(tema).execute();
            exibir();
            ExibirMensagemCommand.sucesso("Configuração salva: tema " + tema.getDescricao().toLowerCase()
                    + " e log em " + formato + ". A próxima operação já será registrada nesse formato.");
        } catch (RuntimeException ex) {
            ExibirMensagemCommand.erro(ex.getMessage());
        }
    }

    public ConfiguracaoView getView() {
        return view;
    }
}
