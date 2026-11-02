package com.br.estimativadeprojetodesoftware.presenter.tabela;

import com.br.estimativadeprojetodesoftware.model.CustoAdicional;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.table.AbstractTableModel;

/** Custos adicionais do projeto (BR11): descrição e valor editáveis. */
public class CustosTableModel extends AbstractTableModel {

    private final Projeto projeto;
    private final Consumer<String> aoErro;
    private boolean editavel;

    public CustosTableModel(Projeto projeto, Consumer<String> aoErro) {
        this.projeto = projeto;
        this.aoErro = aoErro;
    }

    private List<CustoAdicional> custos() {
        return projeto.getCustosAdicionais();
    }

    public CustoAdicional getLinha(int row) {
        return custos().get(row);
    }

    public void setEditavel(boolean editavel) {
        this.editavel = editavel;
        fireTableDataChanged();
    }

    public void recarregar() {
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return custos().size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? "Descrição" : "Valor (R$)";
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return editavel;
    }

    @Override
    public Object getValueAt(int row, int col) {
        CustoAdicional c = custos().get(row);
        return col == 0 ? c.getDescricao() : FormatoValor.reais(c.getValor());
    }

    @Override
    public void setValueAt(Object valor, int row, int col) {
        CustoAdicional c = custos().get(row);
        try {
            if (col == 0) {
                c.setDescricao(String.valueOf(valor));
            } else {
                c.setValor(FormatoValor.decimal(String.valueOf(valor)));
            }
        } catch (IllegalArgumentException e) {
            aoErro.accept(e.getMessage());
        }
        fireTableRowsUpdated(row, row);
    }
}
