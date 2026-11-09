package com.br.estimativadeprojetodesoftware.presenter.tabela;

import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.ItemProjeto;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.TipoSelecao;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.table.AbstractTableModel;

/**
 * Funcionalidades do projeto: seleção e valores por plataforma. Marcar um item
 * de escolha única desmarca os demais da categoria; células "NA" não somam e
 * itens não aplicáveis em todas as plataformas não podem ser marcados.
 */
public class ItensProjetoTableModel extends AbstractTableModel {

    public static final int COLUNA_SELECIONAR = 0;
    public static final int COLUNA_CATEGORIA = 1;
    public static final int COLUNA_NOME = 2;

    private static final String[] COLUNAS = {"Selecionar", "Categoria", "Funcionalidade", "Seleção",
        "Web/Back-end", "iOS", "Android"};
    private static final Plataforma[] PLATAFORMAS = {Plataforma.WEB_BACKEND, Plataforma.IOS, Plataforma.ANDROID};

    private final Projeto projeto;
    private final Consumer<String> aoErro;
    private List<ItemProjeto> itens;
    private boolean editavel;
    private Runnable aoAlterar = () -> {
    };

    public ItensProjetoTableModel(Projeto projeto, Consumer<String> aoErro) {
        this.projeto = projeto;
        this.aoErro = aoErro;
        this.itens = projeto.getItens();
    }

    /** Avisado quando o usuário muda um valor; eventos programáticos da tabela não contam. */
    public void setAoAlterar(Runnable aoAlterar) {
        this.aoAlterar = aoAlterar;
    }

    public boolean isSelecionado(int linha) {
        return itens.get(linha).isSelecionado();
    }

    public TipoSelecao getTipoSelecao(int linha) {
        return itens.get(linha).getFuncionalidade().getTipoSelecao();
    }

    public int contarSelecionados() {
        return (int) itens.stream().filter(ItemProjeto::isSelecionado).count();
    }

    public void setEditavel(boolean editavel) {
        this.editavel = editavel;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return itens.size();
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
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Boolean.class : String.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (!editavel) {
            return false;
        }
        Funcionalidade f = itens.get(row).getFuncionalidade();
        if (col == 0) {
            return f.getTipoSelecao() != TipoSelecao.LIVRE;
        }
        return col >= 4;
    }

    @Override
    public Object getValueAt(int row, int col) {
        ItemProjeto item = itens.get(row);
        Funcionalidade f = item.getFuncionalidade();
        return switch (col) {
            case 0 -> item.isSelecionado();
            case 1 -> f.getCategoria();
            case 2 -> f.getNome();
            case 3 -> descricaoSelecao(f.getTipoSelecao());
            default -> FormatoValor.texto(f, PLATAFORMAS[col - 4]);
        };
    }

    @Override
    public void setValueAt(Object valor, int row, int col) {
        Funcionalidade f = itens.get(row).getFuncionalidade();
        Object antes = getValueAt(row, col);
        try {
            if (col == 0) {
                projeto.selecionar(f.getId(), Boolean.TRUE.equals(valor));
            } else if (col >= 4) {
                f.setValor(PLATAFORMAS[col - 4], FormatoValor.interpretar(valor, f.getTipoValor()));
            }
        } catch (IllegalArgumentException e) {
            aoErro.accept(e.getMessage());
        }
        fireTableDataChanged();
        if (!Objects.equals(antes, getValueAt(row, col))) {
            aoAlterar.run();
        }
    }

    private static String descricaoSelecao(TipoSelecao tipo) {
        return switch (tipo) {
            case UNICA -> "Escolha única";
            case MULTIPLA -> "Múltipla";
            case LIVRE -> "Dias digitados";
            case OPCIONAL -> "Opcional";
        };
    }
}
