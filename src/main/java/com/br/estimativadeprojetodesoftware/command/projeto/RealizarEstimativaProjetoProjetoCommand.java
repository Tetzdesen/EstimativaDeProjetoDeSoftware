package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.view.projeto.DetalheProjetoView;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author tetzner
 */
public class RealizarEstimativaProjetoProjetoCommand implements ProjetoCommand {

    private final ProjetoRepositoryService projetoService;
    private final Projeto projeto;
    private final DetalheProjetoView view;

    public RealizarEstimativaProjetoProjetoCommand(ProjetoRepositoryService projetoService, Projeto projeto, DetalheProjetoView view) {
        this.projetoService = projetoService;
        this.projeto = projeto;
        this.view = view;
    }

    @Override
    public void execute() {
        atualizarCamposDoProjeto(); 

        projetoService.atualizar(projeto); 

        new CarregarDetalhesProjetoProjetoCommand(view, projeto, true).execute(); 
    }

    private void atualizarCamposDoProjeto() {
        JTable tabela = view.getTabelaDetalhes();
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            String nomeCampo = (String) model.getValueAt(i, 0); 
            int diasAndroid = extrairValorComoInteiro(model.getValueAt(i, 1)); 
            int diasIOS = extrairValorComoInteiro(model.getValueAt(i, 2)); 

            for (Campo campo : projeto.getCampos()) {
                if (campo.getNome().equalsIgnoreCase(nomeCampo)) {
                    double mediaDias = (diasAndroid + diasIOS) / 2.0;
                    campo.setDias(mediaDias);
                    break;
                }
            }
        }
    }

    private int extrairValorComoInteiro(Object valor) {
        if (valor == null) {
            return 0; 
        }
        if (valor instanceof Integer) {
            return (Integer) valor;
        }
        if (valor instanceof String) {
            try {
                return Integer.parseInt(((String) valor).trim()); 
            } catch (NumberFormatException e) {
                return 0; 
            }
        }
        return 0; 
    }
}
