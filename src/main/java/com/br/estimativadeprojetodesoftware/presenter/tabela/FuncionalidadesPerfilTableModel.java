package com.br.estimativadeprojetodesoftware.presenter.tabela;

import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.TipoSelecao;
import com.br.estimativadeprojetodesoftware.model.TipoValor;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.table.AbstractTableModel;

/** Funcionalidades de um perfil, editáveis no modo de edição. */
public class FuncionalidadesPerfilTableModel extends AbstractTableModel {

    private static final String[] COLUNAS = {"Categoria", "Funcionalidade", "Seleção", "Tipo de valor",
        "Web/Back-end", "iOS", "Android"};
    private static final Plataforma[] PLATAFORMAS = {Plataforma.WEB_BACKEND, Plataforma.IOS, Plataforma.ANDROID};

    private final Perfil perfil;
    private final Consumer<String> aoErro;
    private List<Funcionalidade> linhas;
    private boolean editavel;

    public FuncionalidadesPerfilTableModel(Perfil perfil, Consumer<String> aoErro) {
        this.perfil = perfil;
        this.aoErro = aoErro;
        recarregar();
    }

    public final void recarregar() {
        this.linhas = perfil.getFuncionalidades();
        fireTableDataChanged();
    }

    public Funcionalidade getLinha(int row) {
        return linhas.get(row);
    }

    public void setEditavel(boolean editavel) {
        this.editavel = editavel;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return linhas.size();
    }

    @Override
    public int getColumnCount() {
        return COLUNAS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUNAS[column];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return editavel;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Funcionalidade f = linhas.get(row);
        return switch (col) {
            case 0 -> f.getCategoria();
            case 1 -> f.getNome();
            case 2 -> f.getTipoSelecao();
            case 3 -> f.getTipoValor();
            default -> FormatoValor.texto(f, PLATAFORMAS[col - 4]);
        };
    }

    @Override
    public void setValueAt(Object valor, int row, int col) {
        Funcionalidade f = linhas.get(row);
        try {
            switch (col) {
                case 0 -> f.setCategoria(String.valueOf(valor));
                case 1 -> f.setNome(String.valueOf(valor));
                case 2 -> f.setTipoSelecao(valor instanceof TipoSelecao t ? t : TipoSelecao.valueOf(String.valueOf(valor)));
                case 3 -> f.setTipoValor(valor instanceof TipoValor t ? t : TipoValor.valueOf(String.valueOf(valor)));
                default -> f.setValor(PLATAFORMAS[col - 4], FormatoValor.interpretar(valor, f.getTipoValor()));
            }
        } catch (IllegalArgumentException e) {
            aoErro.accept(e.getMessage());
        }
        fireTableRowsUpdated(row, row);
    }
}
