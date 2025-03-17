package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.projeto.DetalheProjetoPresenter;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.view.projeto.DetalheProjetoView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;


public class RealizarEstimativaProjetoProjetoCommand implements ProjetoCommand {

    private final DetalheProjetoPresenter detalheProjetoPresenter;
    private final ProjetoRepositoryService projetoService;
    private final Projeto projeto;
    private final DetalheProjetoView view;

    public RealizarEstimativaProjetoProjetoCommand(DetalheProjetoPresenter detalheProjetoPresenter) {
        this.detalheProjetoPresenter = detalheProjetoPresenter;
        this.projetoService = detalheProjetoPresenter.getProjetoService();
        this.projeto = detalheProjetoPresenter.getProjeto();
        this.view = detalheProjetoPresenter.getView();
    }

    @Override
    public void execute() {
        atualizarCamposDoProjeto();
    }

    private void atualizarCamposDoProjeto() {
        JTable tabela = view.getTabelaDetalhes();
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        List<PerfilProjeto> perfis = projeto.getPerfis();
        boolean houveAlteracao = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            String nomeCampo = (String) model.getValueAt(i, 0); 

            for (int j = 1; j <= perfis.size(); j++) { 
                Object valorCelula = model.getValueAt(i, j);
                int diasNovos = extrairValorComoInteiro(valorCelula);

                if (valorCelula == null || valorCelula.toString().isEmpty()) {
                    continue; 
                }

                PerfilProjeto perfil = perfis.get(j - 1); 

                if (perfil.getFuncionalidades().containsKey(nomeCampo)) {
                    int diasOriginais = perfil.getFuncionalidades().get(nomeCampo);

 
                    if (diasNovos != diasOriginais) {
                        Map<String, Integer> funcionalidadesAtualizadas = perfil.getFuncionalidades();
                        funcionalidadesAtualizadas.put(nomeCampo, diasNovos);
                        houveAlteracao = true; 
                    }
                }
            }
        }

        if (houveAlteracao) {
            projetoService.atualizar(projeto);
           // detalheProjetoPresenter.setProjeto(projeto);
            new CarregarDetalhesProjetoProjetoCommand(view, projeto, true).execute(); // Recarrega os dados
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
