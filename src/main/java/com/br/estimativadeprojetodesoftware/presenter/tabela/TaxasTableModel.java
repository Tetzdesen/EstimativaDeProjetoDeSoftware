package com.br.estimativadeprojetodesoftware.presenter.tabela;

import com.br.estimativadeprojetodesoftware.model.Papel;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.TaxaDiaria;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.table.AbstractTableModel;

/** Taxas diárias por papel (linhas) e plataforma (colunas). */
public class TaxasTableModel extends AbstractTableModel {

    private static final Plataforma[] PLATAFORMAS = {Plataforma.WEB_BACKEND, Plataforma.IOS, Plataforma.ANDROID};

    private final Supplier<TaxaDiaria> taxas;
    private final Consumer<String> aoErro;
    private boolean editavel;

    public TaxasTableModel(Supplier<TaxaDiaria> taxas, Consumer<String> aoErro) {
        this.taxas = taxas;
        this.aoErro = aoErro;
    }

    public void setEditavel(boolean editavel) {
        this.editavel = editavel;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return Papel.values().length;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? "Papel" : PLATAFORMAS[column - 1].getDescricao() + " (R$/dia)";
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return editavel && col > 0;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Papel papel = Papel.values()[row];
        return col == 0 ? papel.getDescricao() : FormatoValor.reais(taxas.get().get(PLATAFORMAS[col - 1], papel));
    }

    @Override
    public void setValueAt(Object valor, int row, int col) {
        try {
            taxas.get().set(PLATAFORMAS[col - 1], Papel.values()[row], FormatoValor.decimal(String.valueOf(valor)));
        } catch (IllegalArgumentException e) {
            aoErro.accept(e.getMessage());
        }
        fireTableCellUpdated(row, col);
    }
}
