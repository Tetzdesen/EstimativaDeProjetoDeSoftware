package com.br.estimativadeprojetodesoftware.view.componentes;

/**
 * Item de lista com um rótulo e, opcionalmente, uma quantidade alinhada à
 * direita; subitens aparecem recuados.
 *
 * @param quantidade {@code null} quando o item não tem contador
 */
public record ItemComContador(String rotulo, Integer quantidade, boolean subitem) {

    public ItemComContador(String rotulo, int quantidade) {
        this(rotulo, quantidade, false);
    }

    @Override
    public String toString() {
        return quantidade == null ? rotulo : rotulo + " (" + quantidade + ")";
    }
}
