package com.br.estimativadeprojetodesoftware.presenter.tabela;

import com.br.estimativadeprojetodesoftware.model.CustoAdicional;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.table.AbstractTableModel;

/** Custos adicionais do projeto (BR11): descrição e valor editáveis. */
public class CustosTableModel extends AbstractTableModel {

    private final Projeto projeto;
    private final Consumer<String> aoErro;
    private boolean editavel;
    private Runnable aoAlterar = () -> {
    };

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

    /** Avisado quando o usuário muda um valor; eventos programáticos da tabela não contam. */
    public void setAoAlterar(Runnable aoAlterar) {
        this.aoAlterar = aoAlterar;
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
        String descricaoAntes = c.getDescricao();
        BigDecimal valorAntes = c.getValor();
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
        if (!descricaoAntes.equals(c.getDescricao()) || valorAntes.compareTo(c.getValor()) != 0) {
            aoAlterar.run();
        }
    }
}
