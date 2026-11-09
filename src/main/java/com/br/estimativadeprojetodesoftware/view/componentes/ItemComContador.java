package com.br.estimativadeprojetodesoftware.view.componentes;

/**
 * Item de lista com um rótulo e, opcionalmente, uma quantidade alinhada à
 * direita; subitens aparecem recuados e itens concluídos ganham uma marca.
 *
 * @param quantidade {@code null} quando o item não tem contador
 * @param concluido seção já preenchida (ex.: plataforma marcada)
 */
public record ItemComContador(String rotulo, Integer quantidade, boolean subitem, boolean concluido) {

    public ItemComContador(String rotulo, Integer quantidade, boolean subitem) {
        this(rotulo, quantidade, subitem, false);
    }

    public ItemComContador(String rotulo, int quantidade) {
        this(rotulo, quantidade, false, false);
    }

    @Override
    public String toString() {
        return quantidade == null ? rotulo : rotulo + " (" + quantidade + ")";
    }
}
