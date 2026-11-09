package com.br.estimativadeprojetodesoftware.presenter.tabela;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.suporte.Cenarios;
import com.br.estimativadeprojetodesoftware.view.componentes.ItemComContador;
import com.br.estimativadeprojetodesoftware.view.projeto.SecaoProjeto;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NavegacaoProjetoModelTest {

    private Projeto projeto;
    private ItensProjetoTableModel itens;
    private NavegacaoProjetoModel navegacao;

    @BeforeEach
    void preparar() {
        projeto = Cenarios.projetoVazio(Plataforma.WEB_BACKEND);
        itens = new ItensProjetoTableModel(projeto, e -> fail(e));
        itens.setEditavel(true);
        navegacao = new NavegacaoProjetoModel(itens);
        itens.addTableModelListener(e -> navegacao.recontar());
    }

    private int linha(String categoria, String nome) {
        for (int i = 0; i < itens.getRowCount(); i++) {
            if (categoria.equals(itens.getValueAt(i, 1)) && nome.equals(itens.getValueAt(i, 2))) {
                return i;
            }
        }
        throw new IllegalArgumentException(nome);
    }

    @Test
    void secoesECategoriasNaOrdem() {
        List<String> rotulos = new ArrayList<>();
        for (int i = 0; i < navegacao.getSize(); i++) {
            rotulos.add(navegacao.getElementAt(i).rotulo());
        }
        assertEquals(List.of("Plataformas", "Tamanho e UI", "Funcionalidades",
                "Usuários e Contas", "Conteúdo Gerado pelo Usuário", "Datas e Localizações", "Social e Engajamento",
                "Faturamento e e-Commerce", "Administração, Feedback e Análises", "APIs Externas e Integrações",
                "Segurança", "Desenvolvimento Específico do App", "Recursos Móveis Específicos (iOS)",
                "Recursos Móveis Específicos (Android)",
                "Taxas diárias", "Custos e preço", "Detalhamento"), rotulos);
        assertEquals(List.of("Tamanho do App", "Nível de UI", "Gerente de Projeto"), navegacao.getCategoriasDeConfiguracao());
    }

    @Test
    void contadoresSoEmFuncionalidadesECategorias() {
        for (int i = 0; i < navegacao.getSize(); i++) {
            ItemComContador item = navegacao.getElementAt(i);
            boolean deFuncionalidades = navegacao.getSecao(i) == SecaoProjeto.FUNCIONALIDADES;
            assertEquals(deFuncionalidades, item.quantidade() != null, item.rotulo());
            assertEquals(navegacao.getCategoria(i) != null, item.subitem(), item.rotulo());
        }
    }

    @Test
    void totalNaoContaAsCategoriasDeConfiguracao() {
        itens.setValueAt(true, linha("Tamanho do App", "Médio"), 0);
        itens.setValueAt(true, linha("Gerente de Projeto", "Gerente de Projeto"), 0);
        itens.setValueAt(true, linha("Usuários e Contas", "Cadastro por E-mail e Senha"), 0);
        itens.setValueAt(true, linha("Usuários e Contas", "Cadastro pelo Google"), 0);
        assertEquals(2, navegacao.getSelecionadas(null));
        assertEquals(2, navegacao.getSelecionadas("Usuários e Contas"));
        assertEquals(0, navegacao.getSelecionadas("Segurança"));
        assertThrows(IllegalArgumentException.class, () -> navegacao.getSelecionadas("Tamanho do App"));
    }

    @Test
    void recontarNaoMudaAsEntradas() {
        int tamanho = navegacao.getSize();
        List<ListDataEvent> eventos = new ArrayList<>();
        navegacao.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                fail("entrada inserida");
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                fail("entrada removida");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                eventos.add(e);
            }
        });
        itens.setValueAt(true, linha("Segurança", "Autenticação em Duas Etapas"), 0);
        assertEquals(1, navegacao.getSelecionadas("Segurança"));
        assertEquals(1, navegacao.getSelecionadas(null));
        assertEquals(tamanho, navegacao.getSize());
        assertFalse(eventos.isEmpty());
    }

    @Test
    void indicesDeSecoesECategorias() {
        int seguranca = navegacao.indiceDe(SecaoProjeto.FUNCIONALIDADES, "Segurança");
        assertEquals("Segurança", navegacao.getCategoria(seguranca));
        assertEquals(SecaoProjeto.FUNCIONALIDADES, navegacao.getSecao(seguranca));
        int taxas = navegacao.indiceDe(SecaoProjeto.TAXAS, null);
        assertEquals(SecaoProjeto.TAXAS, navegacao.getSecao(taxas));
        assertNull(navegacao.getCategoria(taxas));
        assertEquals(navegacao.indiceDe(SecaoProjeto.FUNCIONALIDADES, null),
                navegacao.indiceDe(SecaoProjeto.FUNCIONALIDADES, "Inexistente"));
    }
}
