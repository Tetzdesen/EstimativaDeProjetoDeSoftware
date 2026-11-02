package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio.Entidade;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio.Tipo;
import com.br.estimativadeprojetodesoftware.observer.Subject;
import com.br.estimativadeprojetodesoftware.repository.ICompartilhamentoRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Criação e manutenção de projetos (BR03, BR08). Só o dono altera, exclui e
 * calcula; destinatários de compartilhamento só leem. Criação, alteração e
 * exclusão são registradas no log, com sucesso ou falha.
 */
public class ProjetoService extends ServicoObservavel {

    private final IProjetoRepository projetos;
    private final IPerfilRepository perfis;
    private final ICompartilhamentoRepository compartilhamentos;
    private final RegistroOperacoes log;
    private final CalculoEstimativaService calculo;

    public ProjetoService(IProjetoRepository projetos, IPerfilRepository perfis, ICompartilhamentoRepository compartilhamentos,
            RegistroOperacoes log, CalculoEstimativaService calculo, Subject publicador) {
        super(publicador);
        this.projetos = projetos;
        this.perfis = perfis;
        this.compartilhamentos = compartilhamentos;
        this.log = log;
        this.calculo = calculo;
    }

    public ProjetoService() {
        this(SeletorFabricaRepository.obterInstancia(), LogService.getInstancia());
    }

    private ProjetoService(FabricaRepository fabrica, RegistroOperacoes log) {
        this(fabrica.criarProjetoRepository(), fabrica.criarPerfilRepository(), fabrica.criarCompartilhamentoRepository(),
                log, new CalculoEstimativaService(), null);
    }

    public Projeto criar(String nome, List<UUID> idsPerfis, Usuario usuario) {
        String nomeLog = nome == null || nome.isBlank() ? "(sem nome)" : nome.trim();
        Projeto projeto = registrar(OperacaoLog.CRIACAO, nomeLog, usuario, () -> {
            if (idsPerfis == null || idsPerfis.isEmpty()) {
                throw new IllegalArgumentException("Selecione ao menos um perfil para criar o projeto.");
            }
            List<Perfil> selecionados = new ArrayList<>();
            for (UUID id : idsPerfis) {
                selecionados.add(perfis.buscarPorId(id)
                        .orElseThrow(() -> new IllegalArgumentException("Perfil selecionado não existe mais.")));
            }
            Projeto novo = Projeto.aPartirDePerfis(nome, usuario.getId(), selecionados);
            verificarNomeUnico(novo);
            novo.setDonoNome(usuario.getNome());
            projetos.salvar(novo);
            return novo;
        });
        publicar(Tipo.CRIACAO, Entidade.PROJETO, projeto.getId());
        return projeto;
    }

    /** Persiste a alteração feita pelo dono. */
    public void salvar(Projeto projeto, Usuario usuario) {
        registrar(OperacaoLog.ALTERACAO, projeto.getNome(), usuario, () -> {
            verificarDono(projeto, usuario, "alterar");
            verificarNomeUnico(projeto);
            projeto.setAtualizadoEm(LocalDateTime.now());
            projetos.salvar(projeto);
            return null;
        });
        publicar(Tipo.ALTERACAO, Entidade.PROJETO, projeto.getId());
    }

    public void excluir(UUID id, Usuario usuario) {
        Projeto projeto = projetos.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado."));
        registrar(OperacaoLog.EXCLUSAO, projeto.getNome(), usuario, () -> {
            verificarDono(projeto, usuario, "excluir");
            projetos.remover(id);
            return null;
        });
        publicar(Tipo.EXCLUSAO, Entidade.PROJETO, id);
    }

    /** Calcula, marca o projeto como calculado e persiste. */
    public ResultadoEstimativa calcular(Projeto projeto, Usuario usuario) {
        verificarDono(projeto, usuario, "calcular");
        ResultadoEstimativa resultado = calculo.calcular(projeto);
        projeto.setCalculado(true);
        salvar(projeto, usuario);
        return resultado;
    }

    /** Resultado do estado atual do projeto, sem persistir (usado na exibição e na exportação). */
    public ResultadoEstimativa resultado(Projeto projeto) {
        return calculo.calcular(projeto);
    }

    /** Projeto para leitura: o dono ou um destinatário do compartilhamento. */
    public Projeto buscarParaVisualizacao(UUID id, Usuario usuario) {
        Projeto projeto = projetos.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado. Ele pode ter sido excluído."));
        if (!projeto.isDono(usuario.getId()) && !compartilhamentos.estaCompartilhado(id, usuario.getId())) {
            throw new IllegalStateException("Você não tem acesso a este projeto.");
        }
        return projeto;
    }

    public boolean podeEditar(Projeto projeto, Usuario usuario) {
        return usuario != null && projeto.isDono(usuario.getId());
    }

    public List<Projeto> listarProprios(Usuario usuario) {
        return projetos.buscarPorDono(usuario.getId());
    }

    public List<Projeto> listarCompartilhadosComigo(Usuario usuario) {
        List<Projeto> lista = new ArrayList<>();
        for (UUID id : compartilhamentos.buscarProjetosCompartilhadosCom(usuario.getId())) {
            projetos.buscarPorId(id).ifPresent(lista::add);
        }
        return lista;
    }

    private void verificarDono(Projeto projeto, Usuario usuario, String acao) {
        if (!podeEditar(projeto, usuario)) {
            throw new IllegalStateException("Somente o dono pode " + acao + " o projeto \"" + projeto.getNome() + "\".");
        }
    }

    private void verificarNomeUnico(Projeto projeto) {
        if (projetos.existeNomeParaDono(projeto.getNome(), projeto.getDonoId(), projeto.getId())) {
            throw new IllegalArgumentException("Você já tem um projeto chamado \"" + projeto.getNome() + "\".");
        }
    }

    private <T> T registrar(String operacao, String nomeProjeto, Usuario usuario, Supplier<T> acao) {
        String nomeUsuario = usuario == null ? "desconhecido" : usuario.getNome();
        try {
            T resultado = acao.get();
            log.sucesso(operacao, nomeProjeto, nomeUsuario);
            return resultado;
        } catch (RuntimeException e) {
            log.falha(operacao, nomeProjeto, nomeUsuario, e.getMessage());
            throw e;
        }
    }
}
