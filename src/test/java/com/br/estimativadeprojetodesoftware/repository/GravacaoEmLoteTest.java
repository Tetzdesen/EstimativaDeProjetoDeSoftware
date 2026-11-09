package com.br.estimativadeprojetodesoftware.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.ItemProjeto;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import com.br.estimativadeprojetodesoftware.suporte.Cenarios;
import com.br.estimativadeprojetodesoftware.suporte.ContadorDeIdas;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Salvar não pode depender da quantidade de funcionalidades: cada ida ao banco custa ~23 ms no Supabase. */
class GravacaoEmLoteTest {

    private static final int LIMITE_IDAS_PROJETO = 15;

    @TempDir
    Path dir;

    private ContadorDeIdas contador;
    private Usuario dono;

    @BeforeEach
    void preparar() {
        contador = new ContadorDeIdas(BancoTeste.abrir("H2", dir));
        dono = new Usuario(UUID.randomUUID(), "Dono", "dono@x.com", "hash", LocalDateTime.now());
        new JdbcUsuarioRepository(contador.getConexao()).salvar(dono);
    }

    @Test
    void salvarProjetoDoPerfilPadraoEmPoucasIdas() {
        var repo = new JdbcProjetoRepository(contador.getConexao());
        Projeto projeto = Cenarios.prototipoPlanilha(dono.getId());
        assertEquals(71, projeto.getItens().size());

        contador.zerar();
        repo.salvar(projeto);
        int primeira = contador.getIdas();

        contador.zerar();
        repo.salvar(projeto); // agora é uma atualização
        int atualizacao = contador.getIdas();

        assertTrue(primeira <= LIMITE_IDAS_PROJETO, "criar: " + primeira + " idas");
        assertTrue(atualizacao <= LIMITE_IDAS_PROJETO, "atualizar: " + atualizacao + " idas");
        Projeto relido = repo.buscarPorId(projeto.getId()).orElseThrow();
        assertEquals(projeto.getItens().size(), relido.getItens().size());
        for (int i = 0; i < projeto.getItens().size(); i++) {
            ItemProjeto a = projeto.getItens().get(i);
            ItemProjeto b = relido.getItens().get(i);
            assertEquals(a.isSelecionado(), b.isSelecionado(), a.getFuncionalidade().getNome());
            for (Plataforma p : Plataforma.values()) {
                assertEquals(a.getFuncionalidade().getValor(p), b.getFuncionalidade().getValor(p), a.getFuncionalidade().getNome());
            }
        }
        assertEquals(projeto.getCustosAdicionais().size(), relido.getCustosAdicionais().size());
    }

    @Test
    void perfilMaiorUsaAsMesmasIdas() {
        var repo = new JdbcPerfilRepository(contador.getConexao());
        Perfil normal = Cenarios.perfilPadrao().clonar("Normal", dono.getId());
        Perfil dobro = Cenarios.perfilPadrao().clonar("Dobro", dono.getId());
        for (Funcionalidade f : Cenarios.perfilPadrao().getFuncionalidades()) {
            Funcionalidade extra = f.copiar();
            extra.setCategoria(f.getCategoria() + " (extra)");
            dobro.adicionarFuncionalidade(extra);
        }
        assertEquals(2 * normal.getFuncionalidades().size(), dobro.getFuncionalidades().size());

        contador.zerar();
        repo.salvar(normal);
        int idasNormal = contador.getIdas();
        contador.zerar();
        repo.salvar(dobro);

        assertEquals(idasNormal, contador.getIdas());
        assertEquals(dobro.getFuncionalidades().size(), repo.buscarPorId(dobro.getId()).orElseThrow().getFuncionalidades().size());
    }

    @Test
    void falhaNoLoteNaoAlteraOProjeto() {
        var repo = new JdbcProjetoRepository(contador.getConexao());
        Projeto projeto = Cenarios.prototipoPlanilha(dono.getId());
        repo.salvar(projeto);
        int selecionadasAntes = (int) repo.buscarPorId(projeto.getId()).orElseThrow().getItens().stream()
                .filter(ItemProjeto::isSelecionado).count();

        projeto.getItens().forEach(i -> projeto.selecionar(i.getFuncionalidade().getId(), false));
        projeto.setNome("Renomeado");
        contador.falharLoteSe(sql -> sql.contains("projeto_valor"));

        assertThrows(RepositorioException.class, () -> repo.salvar(projeto));

        contador.falharLoteSe(sql -> false);
        Projeto relido = repo.buscarPorId(projeto.getId()).orElseThrow();
        assertNotEquals("Renomeado", relido.getNome());
        assertEquals(selecionadasAntes, relido.getItens().stream().filter(ItemProjeto::isSelecionado).count());
        assertEquals(71, relido.getItens().size());
    }
}
