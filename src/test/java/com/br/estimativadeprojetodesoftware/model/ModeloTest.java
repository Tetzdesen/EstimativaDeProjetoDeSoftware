package com.br.estimativadeprojetodesoftware.model;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.suporte.Cenarios;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModeloTest {

    @Test
    void naoAplicavelEDiferenteDeZero() {
        Funcionalidade f = new Funcionalidade("Cat", "F", TipoSelecao.MULTIPLA, TipoValor.DIAS, 1);
        f.setValor(Plataforma.WEB_BACKEND, BigDecimal.ZERO);
        assertTrue(f.isAplicavel(Plataforma.WEB_BACKEND));
        assertEquals(BigDecimal.ZERO, f.getValor(Plataforma.WEB_BACKEND).orElseThrow());
        assertFalse(f.isAplicavel(Plataforma.IOS));
        assertTrue(f.getValor(Plataforma.IOS).isEmpty());
    }

    @Test
    void validacoesBasicas() {
        Funcionalidade f = new Funcionalidade("Cat", "F", TipoSelecao.MULTIPLA, TipoValor.DIAS, 1);
        assertThrows(IllegalArgumentException.class, () -> f.setValor(Plataforma.IOS, new BigDecimal("-1")));
        assertThrows(IllegalArgumentException.class, () -> new Funcionalidade(" ", "F", TipoSelecao.MULTIPLA, TipoValor.DIAS, 1));
        assertThrows(IllegalArgumentException.class, () -> new Perfil("", UUID.randomUUID()));
        assertThrows(IllegalArgumentException.class, () -> new CustoAdicional("X", new BigDecimal("-5")));
        Perfil perfil = new Perfil("P", UUID.randomUUID());
        perfil.adicionarFuncionalidade(new Funcionalidade("Cat", "F", TipoSelecao.MULTIPLA, TipoValor.DIAS, 1));
        assertThrows(IllegalArgumentException.class,
                () -> perfil.adicionarFuncionalidade(new Funcionalidade("cat", "f", TipoSelecao.MULTIPLA, TipoValor.DIAS, 2)));
    }

    @Test
    void perfilPadraoDoCsv() {
        Perfil p = Cenarios.perfilPadrao();
        assertTrue(p.isPadrao());
        assertEquals(new BigDecimal("450.00"), p.getTaxas().get(Plataforma.IOS, Papel.DESENVOLVIMENTO));
        assertEquals(new BigDecimal("450.00"), p.getTaxas().get(Plataforma.ANDROID, Papel.DESIGN));
        assertEquals(new BigDecimal("300.00"), p.getTaxas().get(Plataforma.WEB_BACKEND, Papel.GERENCIA));
    }

    @Test
    void projetoComVariosPerfisTemAsFuncionalidadesDosDois() {
        UUID dono = UUID.randomUUID();
        Perfil a = new Perfil("A", dono);
        a.adicionarFuncionalidade(new Funcionalidade("Cat", "Só no A", TipoSelecao.MULTIPLA, TipoValor.DIAS, 1));
        a.adicionarFuncionalidade(new Funcionalidade("Cat", "Comum", TipoSelecao.MULTIPLA, TipoValor.DIAS, 2));
        Perfil b = new Perfil("B", dono);
        b.adicionarFuncionalidade(new Funcionalidade("Cat", "Só no B", TipoSelecao.MULTIPLA, TipoValor.DIAS, 1));
        b.adicionarFuncionalidade(new Funcionalidade("Cat", "Comum", TipoSelecao.MULTIPLA, TipoValor.DIAS, 2));
        Projeto projeto = Projeto.aPartirDePerfis("Proj", dono, List.of(a, b));
        assertEquals(List.of("Só no A", "Comum", "Só no B"),
                projeto.getItens().stream().map(i -> i.getFuncionalidade().getNome()).toList());
    }

    @Test
    void copiaDoPrototypeEIndependente() {
        Perfil perfil = Cenarios.perfilPadrao();
        Projeto projeto = Projeto.aPartirDePerfis("Proj", UUID.randomUUID(), List.of(perfil));
        Funcionalidade noProjeto = projeto.buscarItem("Datas e Localizações", "Calendário").orElseThrow().getFuncionalidade();
        noProjeto.setValor(Plataforma.WEB_BACKEND, new BigDecimal("99"));
        Funcionalidade noPerfil = perfil.getFuncionalidades().stream()
                .filter(f -> f.getNome().equals("Calendário")).findFirst().orElseThrow();
        assertEquals(new BigDecimal("7"), noPerfil.getValor(Plataforma.WEB_BACKEND).orElseThrow());
        assertNotEquals(noPerfil.getId(), noProjeto.getId());
        projeto.getTaxas().set(Plataforma.IOS, Papel.DESIGN, new BigDecimal("999"));
        assertEquals(new BigDecimal("450.00"), perfil.getTaxas().get(Plataforma.IOS, Papel.DESIGN));
    }
}
