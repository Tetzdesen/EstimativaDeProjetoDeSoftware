package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio.Entidade;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio.Tipo;
import com.br.estimativadeprojetodesoftware.observer.Subject;
import com.br.estimativadeprojetodesoftware.repository.ICompartilhamentoRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Compartilhamento somente leitura (BR05). O destinatário lê sempre a versão
 * atual do projeto, porque não há cópia.
 */
public class CompartilhamentoService extends ServicoObservavel {

    private final ICompartilhamentoRepository compartilhamentos;
    private final IProjetoRepository projetos;
    private final IUsuarioRepository usuarios;
    private final RegistroOperacoes log;

    public CompartilhamentoService(ICompartilhamentoRepository compartilhamentos, IProjetoRepository projetos,
            IUsuarioRepository usuarios, RegistroOperacoes log, Subject publicador) {
        super(publicador);
        this.compartilhamentos = compartilhamentos;
        this.projetos = projetos;
        this.usuarios = usuarios;
        this.log = log;
    }

    public CompartilhamentoService() {
        this(SeletorFabricaRepository.obterInstancia(), LogService.getInstancia());
    }

    private CompartilhamentoService(FabricaRepository f, RegistroOperacoes log) {
        this(f.criarCompartilhamentoRepository(), f.criarProjetoRepository(), f.criarUsuarioRepository(), log, null);
    }

    public void compartilhar(UUID projetoId, UUID destinatarioId, Usuario dono) {
        Projeto projeto = projetos.buscarPorId(projetoId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado."));
        try {
            if (!projeto.isDono(dono.getId())) {
                throw new IllegalStateException("Somente o dono pode compartilhar o projeto.");
            }
            Usuario destinatario = usuarios.buscarPorId(destinatarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não registrado."));
            if (destinatario.getId().equals(dono.getId())) {
                throw new IllegalArgumentException("Você já é o dono deste projeto.");
            }
            if (compartilhamentos.estaCompartilhado(projetoId, destinatarioId)) {
                throw new IllegalArgumentException("O projeto já está compartilhado com " + destinatario.getNome() + ".");
            }
            compartilhamentos.compartilhar(projetoId, destinatarioId);
            log.sucesso(OperacaoLog.COMPARTILHAMENTO, projeto.getNome(), dono.getNome());
        } catch (RuntimeException e) {
            log.falha(OperacaoLog.COMPARTILHAMENTO, projeto.getNome(), dono.getNome(), e.getMessage());
            throw e;
        }
        publicar(Tipo.CRIACAO, Entidade.COMPARTILHAMENTO, projetoId);
    }

    /** Usuários registrados que ainda podem receber o projeto. */
    public List<Usuario> candidatos(UUID projetoId, Usuario dono) {
        List<UUID> jaRecebem = compartilhamentos.buscarDestinatarios(projetoId);
        List<Usuario> lista = new ArrayList<>();
        for (Usuario u : usuarios.buscarTodos()) {
            if (!u.getId().equals(dono.getId()) && !jaRecebem.contains(u.getId())) {
                lista.add(u);
            }
        }
        return lista;
    }

    public List<Usuario> destinatarios(UUID projetoId) {
        List<Usuario> lista = new ArrayList<>();
        for (UUID id : compartilhamentos.buscarDestinatarios(projetoId)) {
            usuarios.buscarPorId(id).ifPresent(lista::add);
        }
        return lista;
    }
}
